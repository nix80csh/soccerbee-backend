package com.soccerbee.api.locker;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.soccerbee.api.locker.LockerDto.ComingUpMatchDto;
import com.soccerbee.api.locker.LockerDto.NoticeNewDto;
import com.soccerbee.api.locker.LockerDto.NoticeSystemDto;
import com.soccerbee.api.locker.LockerDto.PodUbsDto;
import com.soccerbee.api.locker.LockerDto.RecentAnalysisPDto;
import com.soccerbee.api.locker.LockerDto.RecentAnalysisTDto;
import com.soccerbee.api.locker.LockerDto.RequestTeamDto;
import com.soccerbee.api.locker.LockerDto.ScheduleDto;
import com.soccerbee.api.locker.LockerDto.TeamDto;
import com.soccerbee.api.locker.LockerDto.TeamSelectDto;
import com.soccerbee.api.locker.LockerDto.VoteScheduleMatchDto;
import com.soccerbee.api.locker.LockerDto.WeatherDto;
import com.soccerbee.common.EnumResponse;
import com.soccerbee.entity.Account;
import com.soccerbee.entity.Analysis;
import com.soccerbee.entity.Formation;
import com.soccerbee.entity.FormationPK;
import com.soccerbee.entity.Match;
import com.soccerbee.entity.MatchAnalysis;
import com.soccerbee.entity.MatchAnalysisAccount;
import com.soccerbee.entity.MatchAnalysisAccountPK;
import com.soccerbee.entity.MatchAnalysisSessionFormation;
import com.soccerbee.entity.NoticeNew;
import com.soccerbee.entity.NoticeSystem;
import com.soccerbee.entity.ScheduleMatch;
import com.soccerbee.entity.ScheduleMatchPK;
import com.soccerbee.entity.Team;
import com.soccerbee.entity.TeamAccount;
import com.soccerbee.entity.TeamAccountPK;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AccountRepo;
import com.soccerbee.repo.FormationRepo;
import com.soccerbee.repo.MatchAnalysisAccountRepo;
import com.soccerbee.repo.MatchAnalysisRepo;
import com.soccerbee.repo.MatchAnalysisSessionFormationRepo;
import com.soccerbee.repo.MatchRepo;
import com.soccerbee.repo.NoticeNewRepo;
import com.soccerbee.repo.NoticeSystemRepo;
import com.soccerbee.repo.ScheduleMatchRepo;
import com.soccerbee.repo.TeamAccountRepo;
import com.soccerbee.repo.TeamRepo;
import com.soccerbee.repo.TimelineRepo;
import com.soccerbee.util.AmazonS3Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class LockerService {
  @Value("${spring.profiles.active}") private String deployType;
  @Autowired AccountRepo accountRepo;
  @Autowired TeamAccountRepo teamAccountRepo;
  @Autowired TeamRepo teamRepo;
  @Autowired MatchRepo matchRepo;
  @Autowired TimelineRepo timelineRepo;
  @Autowired MatchAnalysisRepo matchAnalysisRepo;
  @Autowired MatchAnalysisAccountRepo matchAnalysisAccountRepo;
  @Autowired ScheduleMatchRepo scheduleMatchRepo;
  @Autowired FormationRepo formationRepo;
  @Autowired MatchAnalysisSessionFormationRepo matchAnalysisSessionFormationRepo;
  @Autowired NoticeNewRepo noticeNewRepo;
  @Autowired NoticeSystemRepo noticeSystemRepo;

  public EnumResponse registTeam(TeamDto teamDto) {
    Account account = accountRepo.findById(teamDto.getIdfAccount())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    Team team = new Team();
    BeanUtils.copyProperties(teamDto, team);
    team.setAccount(account);
    team.setNameAbbr(teamDto.getNameAbbr().toUpperCase());
    teamRepo.saveAndFlush(team);

    // 팀의 구성원으로 자동등록 0번으로 구성
    TeamAccount teamAccount = new TeamAccount();
    TeamAccountPK id = new TeamAccountPK();
    id.setIdfAccount(account.getIdfAccount());
    id.setIdfTeam(team.getIdfTeam());
    teamAccount.setNumber(0);
    teamAccount.setId(id);
    teamAccount.setGrade("O");
    teamAccount.setPosition(account.getAccountPlayer().getPosition());
    teamAccountRepo.save(teamAccount);
    return EnumResponse.Registered;
  }

  public EnumResponse modifyTeam(TeamDto teamDto) {
    Team team = teamRepo.findById(teamDto.getIdfTeam())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Team));

    BeanUtils.copyProperties(teamDto, team);
    teamRepo.save(team);
    return EnumResponse.Modified;
  }

  public List<TeamDto> searchTeam(String searchWord, int idfAccount) {
    accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    List<TeamDto> teamDtoList = new ArrayList<TeamDto>();
    // 검색어가 팀코드일경우에
    if (searchWord.contains("-")
        // 마지막문자가 "-"일 경우를 거름
        && !searchWord.substring(searchWord.length() - 1, searchWord.length()).equals("-")
        // 숫자형 여부를 판단
        && isStringInteger(searchWord.split("-")[1])) {

      String[] splitWord = searchWord.split("-");
      Integer idfTeam = Integer.parseInt(splitWord[1]);
      TeamDto teamDto = new TeamDto();
      Team team = teamRepo.findById(idfTeam).orElse(new Team());
      BeanUtils.copyProperties(team, teamDto);
      teamDto.setNameOwner(team.getAccount().getName());

      TeamAccountPK id = new TeamAccountPK();
      id.setIdfTeam(team.getIdfTeam());
      id.setIdfAccount(idfAccount);
      TeamAccount teamAccount = teamAccountRepo.findById(id).orElse(new TeamAccount());
      teamDto.setGrade(teamAccount.getGrade());
      teamDtoList.add(teamDto);
    } else {
      // 구단주 이름으로 검색 한명의 구단주는 여러개의 팀을 가질수 있다
      List<Account> accountList = accountRepo.findByName(searchWord);
      for (Account account : accountList) {
        for (Team team : account.getTeams()) {
          TeamDto teamDto = new TeamDto();
          BeanUtils.copyProperties(team, teamDto);
          teamDto.setNameOwner(team.getAccount().getName());
          TeamAccountPK id = new TeamAccountPK();
          id.setIdfTeam(team.getIdfTeam());
          id.setIdfAccount(idfAccount);
          TeamAccount teamAccount = teamAccountRepo.findById(id).orElse(new TeamAccount());
          teamDto.setGrade(teamAccount.getGrade());
          teamDtoList.add(teamDto);
        }
      }
    }
    return teamDtoList;
  }

  public EnumResponse requestTeam(RequestTeamDto requestTeamDto) {
    Account account = accountRepo.findById(requestTeamDto.getIdfAccount())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));
    Team team = teamRepo.findById(requestTeamDto.getIdfTeam())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Team));
    TeamAccountPK id = new TeamAccountPK();
    id.setIdfAccount(requestTeamDto.getIdfAccount());
    id.setIdfTeam(requestTeamDto.getIdfTeam());

    TeamAccount teamAccount = new TeamAccount();
    teamAccount.setNumber(getJerseyNumber(team.getIdfTeam()));
    teamAccount.setId(id);
    teamAccount.setGrade("R");
    teamAccount.setPosition(account.getAccountPlayer().getPosition());
    teamAccountRepo.save(teamAccount);
    return EnumResponse.Requested;
  }

  private boolean isStringInteger(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private int getJerseyNumber(int idfTeam) {
    Random random = new Random();
    List<TeamAccount> teamAccountList = teamAccountRepo.findByIdIdfTeam(idfTeam);
    // 1 ~ 99까지 숫자 배열만들기
    int[] defaultNumbers = new int[99];
    for (int i = 0; i < 99; i++) {
      defaultNumbers[i] = i + 1;
    }
    // 중복제거 배열
    int[] removedNumbers = new int[99];
    for (TeamAccount teamAccount : teamAccountList) {
      removedNumbers = ArrayUtils.removeElement(defaultNumbers, teamAccount.getNumber());
    }
    return removedNumbers[random.nextInt(removedNumbers.length - 1)];
  }

  public EnumResponse deleteRequestTeam(RequestTeamDto requestTeamDto) {
    TeamAccountPK id = new TeamAccountPK();
    id.setIdfAccount(requestTeamDto.getIdfAccount());
    id.setIdfTeam(requestTeamDto.getIdfTeam());
    TeamAccount teamAccount = teamAccountRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));
    if (!teamAccount.getGrade().equals("R"))
      throw new LogicException(LogicErrorList.AlreadyConfirmed);

    teamAccountRepo.delete(teamAccount);
    return EnumResponse.Deleted;
  }

  public List<TeamSelectDto> getTeamSelectList(int idfAccount) {
    List<TeamAccount> teamAccountList = teamAccountRepo.findByIdIdfAccount(idfAccount);

    List<TeamAccount> filteredTeamAccountList =
        teamAccountList
            .stream().filter(Objects::nonNull).filter(at -> at.getGrade().equals("O")
                || at.getGrade().equals("M") || at.getGrade().equals("A"))
            .collect(Collectors.toList());

    List<TeamSelectDto> teamSelectDtoList = new ArrayList<TeamSelectDto>();
    for (TeamAccount teamAccount : filteredTeamAccountList) {
      TeamSelectDto teamSelectDto = new TeamSelectDto();
      teamSelectDto.setIdfAccount(teamAccount.getId().getIdfAccount());
      teamSelectDto.setIdfTeam(teamAccount.getId().getIdfTeam());
      teamSelectDto.setName(teamAccount.getTeam().getName());
      teamSelectDto.setImage(teamAccount.getTeam().getImageEmblem());
      teamSelectDto.setTotalPlayer(teamAccount.getTeam().getTeamAccounts().size());
      teamSelectDtoList.add(teamSelectDto);
    }
    return teamSelectDtoList;
  }

  public List<ScheduleDto> getScheduleMatchList(int idfAccount, String yearMonth) {
    Account account = accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    List<ScheduleDto> scheduleDtoList = new ArrayList<ScheduleDto>();
    for (TeamAccount teamAccount : account.getTeamAccounts()) {
      for (ScheduleMatch scheduleMatch : teamAccount.getScheduleMatches()) {
        ScheduleDto scheduleDto = new ScheduleDto();
        Match match = scheduleMatch.getMatch();
        DateFormat df = new SimpleDateFormat("yyyyMM");
        if (df.format(match.getDate()).equals(yearMonth)) {
          BeanUtils.copyProperties(match, scheduleDto);
          scheduleDto.setDate(match.getDate());
          scheduleDto.setIdfTeam(match.getTeamOpponent().getId().getIdfTeam());
          scheduleDto.setIdfTeamOpponent(match.getTeamOpponent().getId().getIdfTeamOpponent());
          scheduleDto.setImage(teamAccount.getTeam().getImageEmblem());
          scheduleDto.setOpponentName(match.getTeamOpponent().getName());
          scheduleDto.setOpponentImage(match.getTeamOpponent().getImage());
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(scheduleDto.getDate());
          scheduleDto.setDay(calendar.get(Calendar.DAY_OF_WEEK));
          scheduleDto.setVote(scheduleMatch.getVote());
          int totalAttendance =
              match.getScheduleMatches().stream().filter(sm -> sm.getVote() != null)
                  .filter(sm -> sm.getAttendance() == true).collect(Collectors.toList()).size();
          scheduleDto.setAttendance(scheduleMatch.getAttendance());
          scheduleDto.setTotalAttendance(totalAttendance);

          MatchAnalysisAccountPK id = new MatchAnalysisAccountPK();
          id.setIdfMatch(match.getIdfMatch());
          id.setIdfAccount(idfAccount);
          scheduleDto.setMyAnalysis(false);
          if (matchAnalysisAccountRepo.existsById(id))
            scheduleDto.setMyAnalysis(true);

          scheduleDto.setTeamAnalysis(false);
          if (match.getMatchAnalysis() != null)
            scheduleDto.setTeamAnalysis(true);

          scheduleDto.setAnalyzedTotalPlayer(
              matchAnalysisAccountRepo.countByIdIdfMatchAndAnalyzed(match.getIdfMatch(), true));
          scheduleDto.setNonAnalyzedTotalPlayer(
              matchAnalysisAccountRepo.countByIdIdfMatchAndAnalyzed(match.getIdfMatch(), false));

          scheduleDtoList.add(scheduleDto);
        }
      }
    }
    return scheduleDtoList;
  }

  public EnumResponse voteScheduleMatch(VoteScheduleMatchDto voteScheduleMatchDto) {
    ScheduleMatchPK idScheduleMatchPK = new ScheduleMatchPK();
    idScheduleMatchPK.setIdfMatch(voteScheduleMatchDto.getIdfMatch());
    idScheduleMatchPK.setIdfTeam(voteScheduleMatchDto.getIdfTeam());
    idScheduleMatchPK.setIdfAccount(voteScheduleMatchDto.getIdfAccount());
    ScheduleMatch scheduleMatch = scheduleMatchRepo.findById(idScheduleMatchPK)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_ScheduleMatch));

    scheduleMatch.setVote(voteScheduleMatchDto.getVote());
    scheduleMatch.setAttendance(voteScheduleMatchDto.getVote());
    scheduleMatchRepo.save(scheduleMatch);

    List<Formation> formationList = new ArrayList<Formation>();
    for (int i = 1; i <= scheduleMatch.getMatch().getSession(); i++) {
      FormationPK idFormationPK = new FormationPK();
      Formation formation = new Formation();
      idFormationPK.setIdfMatch(scheduleMatch.getId().getIdfMatch());
      idFormationPK.setIdfTeam(scheduleMatch.getId().getIdfTeam());
      idFormationPK.setIdfAccount(scheduleMatch.getId().getIdfAccount());
      idFormationPK.setSessionNumber(i);
      formation.setId(idFormationPK);
      formation.setCaptain(false);
      formationList.add(formation);
    }

    if (scheduleMatch.getAttendance() == true) {
      formationRepo.saveAll(formationList);
    } else {
      formationRepo.deleteAll(formationList);
    }
    return EnumResponse.Modified;
  }

  public ComingUpMatchDto getComingUpMatch(int idfAccount) {
    accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    List<ScheduleMatch> scheduleMatchList = scheduleMatchRepo.findByIdIdfAccount(idfAccount);
    List<Match> matchList = new ArrayList<Match>();
    for (ScheduleMatch scheduleMatch : scheduleMatchList) {
      matchList.add(scheduleMatch.getMatch());
    }

    int minsToMinus = 16;
    Date date = new Date();
    date.setTime(date.getTime() - minsToMinus * 60000);
    List<Match> filteredMatchList = matchList.stream()
        .filter(ml -> date.getTime() <= ml.getDate().getTime() + ml.getTimeFinish().getTime())
        .sorted(Comparator.comparing(Match::getDate).thenComparing(Match::getTimeStart))
        .collect(Collectors.toList());
    if (filteredMatchList.size() == 0)
      throw new LogicException(LogicErrorList.DoesNotExist_Match);

    ComingUpMatchDto comingUpMatchDto = new ComingUpMatchDto();
    Match comingUpMatch = filteredMatchList.get(0);
    System.out.println("디비 데이터: "
        + new Date(comingUpMatch.getDate().getTime() + comingUpMatch.getTimeFinish().getTime()));
    BeanUtils.copyProperties(comingUpMatch, comingUpMatchDto);
    comingUpMatchDto.setEmblemTeam(comingUpMatch.getTeamOpponent().getTeam().getImageEmblem());
    comingUpMatchDto.setEmblemOpponent(comingUpMatch.getTeamOpponent().getImage());
    comingUpMatchDto.setIdfTeam(comingUpMatch.getTeamOpponent().getTeam().getIdfTeam());
    comingUpMatchDto.setNameTeam(comingUpMatch.getTeamOpponent().getTeam().getName());
    comingUpMatchDto.setNameOpponent(comingUpMatch.getTeamOpponent().getName());
    List<ScheduleMatch> comingUpScheduleMatchList = comingUpMatch.getScheduleMatches().stream()
        .filter(sm -> sm.getId().getIdfAccount() == idfAccount).collect(Collectors.toList());
    comingUpMatchDto.setVote(comingUpScheduleMatchList.get(0).getVote());
    Integer totalVote =
        comingUpMatch.getScheduleMatches().stream().filter(sm -> sm.getVote() != null)
            .filter(sm -> sm.getVote() == true).collect(Collectors.toList()).size();
    comingUpMatchDto.setTotalVote(totalVote);
    comingUpMatchDto.setTotalPlayer(comingUpMatch.getScheduleMatches().size());
    return comingUpMatchDto;
  }

  public List<PodUbsDto> getPodUbsList(Integer idfAccount) {
    List<S3ObjectSummary> s3ObjectSummaryList =
        AmazonS3Util.getS3ObjectList("soccerbee-" + deployType, "gpsdata/" + idfAccount).stream()
            .filter(s3 -> s3.getKey().split("\\.")[1].equals("ubs")).collect(Collectors.toList());
    List<PodUbsDto> podUbsDtoList = new ArrayList<PodUbsDto>();
    for (S3ObjectSummary s3Object : s3ObjectSummaryList) {
      PodUbsDto podUbsDto = new PodUbsDto();
      podUbsDto.setUploadedDate(s3Object.getLastModified());
      podUbsDto.setUbsName(s3Object.getKey().replace("gpsdata/", ""));
      podUbsDtoList.add(podUbsDto);
    }
    return podUbsDtoList;
  }

  public TeamDto getTeam(int idfTeam) {
    Team team = teamRepo.findById(idfTeam)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Team));

    TeamDto teamDto = new TeamDto();
    BeanUtils.copyProperties(team, teamDto);
    teamDto.setIdfAccount(team.getAccount().getIdfAccount());
    return teamDto;
  }

  public RecentAnalysisPDto getRecentAnalysisP(int idfAccount) {
    Account account = accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.MONTH, -3);
    List<Analysis> analysisList =
        account.getAnalysis().stream().filter(a -> a.getUpdDate().after(calendar.getTime()))
            .sorted(Comparator.comparing((Analysis a) -> a.getUpdDate()).reversed())
            .collect(Collectors.toList());
    RecentAnalysisPDto recentAnalysisPDto = new RecentAnalysisPDto();
    if (analysisList.size() > 0) {
      Analysis analysis = analysisList.get(0);
      calendar.setTime(analysis.getUpdDate());
      recentAnalysisPDto.setImage(account.getAccountPlayer().getImage());
      recentAnalysisPDto.setUbsp(analysis.getUbsp());
      recentAnalysisPDto.setDay(calendar.get(Calendar.DAY_OF_WEEK));
      BigDecimal rate = BigDecimal.valueOf(analysis.getAnalysisSessions().stream()
          .filter(as -> Objects.nonNull(as.getRate()))
          .collect(Collectors.summarizingDouble(as -> as.getRate().doubleValue())).getAverage());
      recentAnalysisPDto.setRate(rate);

      BigDecimal distance = BigDecimal.valueOf(analysis.getAnalysisSessions().stream()
          .filter(as -> Objects.nonNull(as.getDistance()))
          .collect(Collectors.summarizingDouble(as -> as.getDistance().doubleValue())).getSum());
      recentAnalysisPDto.setDistance(distance);

      BigDecimal speedMax = BigDecimal.valueOf(analysis.getAnalysisSessions().stream()
          .filter(as -> Objects.nonNull(as.getSpeedMax()))
          .collect(Collectors.summarizingDouble(as -> as.getDistance().doubleValue())).getMax());
      recentAnalysisPDto.setSpeedMax(speedMax);

      Integer sprint = (int) analysis.getAnalysisSessions().stream()
          .filter(as -> Objects.nonNull(as.getSprint()))
          .collect(Collectors.summarizingInt(as -> as.getSprint())).getSum();
      recentAnalysisPDto.setSprint(sprint);

      BigDecimal coverage = BigDecimal.valueOf(
          analysis.getAnalysisSessions().stream().filter(as -> Objects.nonNull(as.getCoverage()))
              .collect(Collectors.summarizingDouble(as -> as.getCoverage().doubleValue()))
              .getAverage());
      recentAnalysisPDto.setCoverage(coverage);
    }
    return recentAnalysisPDto;
  }

  public RecentAnalysisTDto getRecentAnalysisT(int idfAccount) {
    accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    // 내가 속한 팀리스트
    List<Integer> idfTeamList = teamAccountRepo.findByIdIdfAccount(idfAccount).stream()
        .map(ta -> ta.getTeam().getIdfTeam()).collect(Collectors.toList());

    // 속한 팀이 잡은 일정리스트
    List<Integer> idfMatchList = matchRepo.findByTeamOpponentIdIdfTeamIn(idfTeamList).stream()
        .map(m -> m.getIdfMatch()).collect(Collectors.toList());

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.MONTH, -3);
    // 일정리스트 중 분석된 리스트
    List<MatchAnalysis> matchAnalysisList = matchAnalysisRepo.findByMatchIdfMatchIn(idfMatchList)
        .stream().filter(a -> a.getUpdDate().after(calendar.getTime()))
        .sorted(Comparator.comparing((MatchAnalysis ma) -> ma.getUpdDate()).reversed())
        .collect(Collectors.toList());

    RecentAnalysisTDto recentAnalysisTDto = new RecentAnalysisTDto();
    if (matchAnalysisList.size() > 0) {
      MatchAnalysis matchAnalysis = matchAnalysisList.get(0);
      List<MatchAnalysisAccount> matchAnalysisAccountList = matchAnalysis.getMatchAnalysisAccounts()
          .stream()
          .filter(
              ma -> ma.getAccount().getIdfAccount().equals(idfAccount) && ma.getAnalyzed() == true)
          .collect(Collectors.toList());

      if (matchAnalysisAccountList.size() > 0)
        recentAnalysisTDto.setAnalysed(true);

      recentAnalysisTDto.setIdfMatch(matchAnalysis.getIdfMatch());
      recentAnalysisTDto
          .setIdfTeam(matchAnalysis.getMatch().getTeamOpponent().getId().getIdfTeam());
      recentAnalysisTDto.setDate(matchAnalysis.getTimeStart());

      calendar.setTime(matchAnalysis.getTimeStart());
      recentAnalysisTDto.setDay(calendar.get(Calendar.DAY_OF_WEEK));
      recentAnalysisTDto
          .setEmblem(matchAnalysis.getMatch().getTeamOpponent().getTeam().getImageEmblem());
      recentAnalysisTDto.setEmblemOpponent(matchAnalysis.getMatch().getTeamOpponent().getImage());
      recentAnalysisTDto.setLocation(matchAnalysis.getMatch().getLocation());
      recentAnalysisTDto.setName(matchAnalysis.getMatch().getTeamOpponent().getTeam().getName());
      recentAnalysisTDto.setNameOpponent(matchAnalysis.getMatch().getTeamOpponent().getName());

      List<MatchAnalysisSessionFormation> matchAnalysisSessionFormationList =
          matchAnalysisSessionFormationRepo
              .findByIdIdfMatchAndIdIdfAccount(matchAnalysis.getIdfMatch(), idfAccount);
      BigDecimal rate = BigDecimal.valueOf(matchAnalysisSessionFormationList.stream()
          .filter(as -> Objects.nonNull(as.getRate()))
          .collect(Collectors.summarizingDouble(as -> as.getRate().doubleValue())).getAverage());
      recentAnalysisTDto.setRate(rate);
      recentAnalysisTDto.setTimeStart(matchAnalysis.getTimeStart());
      recentAnalysisTDto.setTimeFinish(matchAnalysis.getTimeFinish());

      int score = timelineRepo.findByIdIdfMatch(matchAnalysis.getIdfMatch()).stream()
          .filter(tl -> tl.getType().equals("GF")).collect(Collectors.toList()).size();
      recentAnalysisTDto.setScore(score);
      int scoreOpponent = timelineRepo.findByIdIdfMatch(matchAnalysis.getIdfMatch()).stream()
          .filter(tl -> tl.getType().equals("GA")).collect(Collectors.toList()).size();
      recentAnalysisTDto.setScoreOpponent(scoreOpponent);
    }
    return recentAnalysisTDto;
  }

  public List<NoticeNewDto> getNewsList() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    List<NoticeNew> noticeNewList = noticeNewRepo.findAll().stream()
        .filter(nn -> nn.getDueDate().after(calendar.getTime())).collect(Collectors.toList());

    List<NoticeNewDto> noticeNewDtoList = new ArrayList<NoticeNewDto>();
    for (NoticeNew noticeNew : noticeNewList) {
      NoticeNewDto noticeNewDto = new NoticeNewDto();
      noticeNewDto.setIdfNoticeNew(noticeNew.getIdfNoticeNew());
      noticeNewDto.setTitle(noticeNew.getTitle());
      noticeNewDto.setImage(noticeNew.getImage());
      noticeNewDtoList.add(noticeNewDto);
    }
    return noticeNewDtoList;
  }

  public NoticeNewDto getNews(int idfNoticeNew) {
    NoticeNew noticeNew = noticeNewRepo.findById(idfNoticeNew)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_NoticeNew));

    NoticeNewDto noticeNewDto = new NoticeNewDto();
    BeanUtils.copyProperties(noticeNew, noticeNewDto);
    return noticeNewDto;
  }

  public List<NoticeSystemDto> getSystemList() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    List<NoticeSystem> noticeSystemList = noticeSystemRepo.findAll();

    List<NoticeSystemDto> noticeSystemDtoList = new ArrayList<NoticeSystemDto>();
    for (NoticeSystem noticeSystem : noticeSystemList) {
      NoticeSystemDto noticeSystemDto = new NoticeSystemDto();
      noticeSystemDto.setIdfNoticeSystem(noticeSystem.getIdfNoticeSystem());
      noticeSystemDto.setTitle(noticeSystem.getTitle());
      noticeSystemDto.setRegDate(noticeSystem.getRegDate());
      noticeSystemDtoList.add(noticeSystemDto);
    }
    return noticeSystemDtoList;
  }

  public NoticeSystemDto getSystem(int idfNoticeSystem) {
    NoticeSystem noticeSystem = noticeSystemRepo.findById(idfNoticeSystem)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_NoticeSystem));

    NoticeSystemDto noticeSystemDto = new NoticeSystemDto();
    BeanUtils.copyProperties(noticeSystem, noticeSystemDto);
    return noticeSystemDto;
  }


  public WeatherDto getMatchDayWeather(int idfMatch) {
    try {
      Match match = matchRepo.findById(idfMatch)
          .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Match));
      String dateString = match.getDate() + " " + match.getTimeFinish();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = sdf.parse(dateString);
      if (date.before(new Date()))
        throw new LogicException(LogicErrorList.Previous_Match);
      long diffTimeInMillis = match.getDate().getTime() - new Date().getTime();
      long days = diffTimeInMillis / (24 * 60 * 60 * 1000);
      if (days > 7)
        throw new LogicException(LogicErrorList.DoesNotExist_Forecast);

      String latitude = match.getLatLong().split(",")[0];
      String longitude = match.getLatLong().split(",")[1];
      String apiUrl = "https://api.openweathermap.org";
      String apiKey = "e984dc288eb90cde901c13a7ee854871";
      HttpResponse<JsonNode> response = Unirest.get(apiUrl + "/data/2.5/onecall?lat=" + latitude
          + "&lon=" + longitude + "&%20exclude=hourly,daily&appid=" + apiKey).asJson();
      // System.out.println("url: " + apiUrl + "/data/2.5/onecall?lat=" +
      // latitude + "&lon="
      // + longitude + "&%20exclude=hourly,daily&appid=" + apiKey);
      JSONArray daily = (JSONArray) response.getBody().getObject().getJSONArray("daily");
      JSONObject dueDayWeatherObject = daily.getJSONObject((int) days);
      String icon =
          (String) dueDayWeatherObject.getJSONArray("weather").getJSONObject(0).get("icon");
      String type =
          (String) dueDayWeatherObject.getJSONArray("weather").getJSONObject(0).get("main");
      Integer id = (Integer) dueDayWeatherObject.getJSONArray("weather").getJSONObject(0).get("id");
      Double temperature =
          Math.round(((Double) dueDayWeatherObject.getJSONObject("temp").get("day") - 273) * 100)
              / 100.0;
      Integer humidity = (Integer) dueDayWeatherObject.get("humidity");
      Double windSpeed = (Double) dueDayWeatherObject.get("wind_speed");
      String pop = dueDayWeatherObject.get("pop").toString();
      Double uvi = (Double) dueDayWeatherObject.get("uvi");

      WeatherDto weatherDto = new WeatherDto();
      weatherDto.setId(id);
      weatherDto.setIcon(icon);
      weatherDto.setType(type);
      weatherDto.setHumidity(humidity);
      weatherDto.setTemperature(temperature);
      weatherDto.setWindSpeed(windSpeed);
      weatherDto.setPop(Double.parseDouble(pop));
      weatherDto.setUvi(uvi);
      return weatherDto;
    } catch (UnirestException | ParseException e) {
      return null;
    }
  }
}
