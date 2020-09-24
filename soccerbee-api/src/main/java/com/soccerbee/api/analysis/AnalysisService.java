package com.soccerbee.api.analysis;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.soccerbee.api.analysis.AnalysisDto.AbilityDto;
import com.soccerbee.api.analysis.AnalysisDto.AnalysisPDto;
import com.soccerbee.api.analysis.AnalysisDto.AnalysisPSessionDto;
import com.soccerbee.api.analysis.AnalysisDto.AnalysisTDto;
import com.soccerbee.api.analysis.AnalysisDto.AnalysisTFormationDto;
import com.soccerbee.api.analysis.AnalysisDto.AnalysisTSessionDto;
import com.soccerbee.api.analysis.AnalysisDto.HitmapDto;
import com.soccerbee.api.analysis.AnalysisDto.MatchAnalysisDto;
import com.soccerbee.api.analysis.AnalysisDto.MatchDto;
import com.soccerbee.api.analysis.AnalysisDto.PStatsDto;
import com.soccerbee.api.analysis.AnalysisDto.PVisualizerDto;
import com.soccerbee.api.analysis.AnalysisDto.PitchPlayerDto;
import com.soccerbee.api.analysis.AnalysisDto.SprintDto;
import com.soccerbee.api.analysis.AnalysisDto.TLineupDto;
import com.soccerbee.api.analysis.AnalysisDto.TSessionStatsDto;
import com.soccerbee.api.analysis.AnalysisDto.TStatsDto;
import com.soccerbee.api.analysis.AnalysisDto.TVisualizerDto;
import com.soccerbee.api.analysis.AnalysisDto.UbstDto;
import com.soccerbee.common.EnumResponse;
import com.soccerbee.entity.Ability;
import com.soccerbee.entity.Account;
import com.soccerbee.entity.AccountDevice;
import com.soccerbee.entity.Analysis;
import com.soccerbee.entity.AnalysisSession;
import com.soccerbee.entity.AnalysisSessionPK;
import com.soccerbee.entity.Formation;
import com.soccerbee.entity.Hitmap;
import com.soccerbee.entity.Match;
import com.soccerbee.entity.MatchAnalysis;
import com.soccerbee.entity.MatchAnalysisAccount;
import com.soccerbee.entity.MatchAnalysisAccountPK;
import com.soccerbee.entity.MatchAnalysisSession;
import com.soccerbee.entity.MatchAnalysisSessionFormation;
import com.soccerbee.entity.MatchAnalysisSessionFormationPK;
import com.soccerbee.entity.MatchAnalysisSessionPK;
import com.soccerbee.entity.MatchHitmap;
import com.soccerbee.entity.MatchSprint;
import com.soccerbee.entity.Sprint;
import com.soccerbee.entity.TeamAccount;
import com.soccerbee.entity.TeamAccountPK;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AbilityRepo;
import com.soccerbee.repo.AccountRepo;
import com.soccerbee.repo.AnalysisRepo;
import com.soccerbee.repo.AnalysisSessionRepo;
import com.soccerbee.repo.FormationRepo;
import com.soccerbee.repo.HitmapRepo;
import com.soccerbee.repo.MatchAnalysisAccountRepo;
import com.soccerbee.repo.MatchAnalysisRepo;
import com.soccerbee.repo.MatchAnalysisSessionFormationRepo;
import com.soccerbee.repo.MatchAnalysisSessionRepo;
import com.soccerbee.repo.MatchHitmapRepo;
import com.soccerbee.repo.MatchRepo;
import com.soccerbee.repo.MatchSprintRepo;
import com.soccerbee.repo.SprintRepo;
import com.soccerbee.repo.TeamAccountRepo;
import com.soccerbee.util.AmazonS3Util;
import com.soccerbee.util.FcmUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AnalysisService {
  @Value("${spring.profiles.active}") private String deployType;
  @Autowired AccountRepo accountRepo;
  @Autowired AbilityRepo abilityRepo;
  @Autowired TeamAccountRepo teamAccountRepo;
  @Autowired MatchRepo matchRepo;
  @Autowired AnalysisRepo analysisRepo;
  @Autowired AnalysisSessionRepo analysisSessionRepo;
  @Autowired HitmapRepo hitmapRepo;
  @Autowired SprintRepo sprintRepo;
  @Autowired FormationRepo formationRepo;
  @Autowired MatchAnalysisRepo matchAnalysisRepo;
  @Autowired MatchHitmapRepo matchHitmapRepo;
  @Autowired MatchSprintRepo matchSprintRepo;
  @Autowired MatchAnalysisAccountRepo matchAnalysisAccountRepo;
  @Autowired MatchAnalysisSessionRepo matchAnalysisSessionRepo;
  @Autowired MatchAnalysisSessionFormationRepo matchAnalysisSessionFormationRepo;
  @Autowired FcmUtil fcmUtil;

  public EnumResponse registP(AnalysisPDto analysisPDto) {
    String bucketName = "soccerbee-" + deployType;
    String ubsPath = "gpsdata/" + analysisPDto.getUbsp().replace("ubsp", "ubs");
    String ubspPath = "gpsdata/" + analysisPDto.getUbsp();
    if (AmazonS3Util.getS3ObjectList(bucketName, ubsPath).size() == 0)
      throw new LogicException(LogicErrorList.DoesNotExist_Ubs);
    if (AmazonS3Util.getS3ObjectList(bucketName, ubspPath).size() > 0)
      throw new LogicException(LogicErrorList.Exist_Ubsp);
    Account account = accountRepo.findById(analysisPDto.getIdfAccount())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));
    String[] idfAccountFromUbsp = analysisPDto.getUbsp().split("\\_");
    if (!idfAccountFromUbsp[0].equals(analysisPDto.getIdfAccount().toString()))
      throw new LogicException(LogicErrorList.NotMatched);

    Analysis analysis = new Analysis();
    BeanUtils.copyProperties(analysisPDto, analysis);
    analysis.setAccount(account);
    analysisRepo.save(analysis);

    for (AnalysisPSessionDto analysisPSessionDto : analysisPDto.getAnalysisPSessionDtoList()) {
      AnalysisSessionPK id = new AnalysisSessionPK();
      id.setUbsp(analysisPDto.getUbsp());
      id.setNumber(analysisPSessionDto.getNumber());
      AnalysisSession analysisSession = new AnalysisSession();
      analysisSession.setId(id);
      BeanUtils.copyProperties(analysisPSessionDto, analysisSession);
      analysisSessionRepo.save(analysisSession);
    }
    AmazonS3Util.copyFile(bucketName, ubsPath, bucketName, ubspPath);
    AmazonS3Util.deleteFile(bucketName, ubsPath);

    return EnumResponse.Registered;
  }

  public AnalysisPDto getPInfo(String ubsp) {
    Analysis analysis = analysisRepo.findById(ubsp)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Analysis));

    AnalysisPDto analysisPDto = new AnalysisPDto();

    if (analysis.getAccount().getAccountPlayer().getImage() != null)
      analysisPDto.setImage(analysis.getAccount().getAccountPlayer().getImage());
    if (analysis.getAccount().getPod() != null)
      analysisPDto.setPodType(analysis.getAccount().getPod().getType());
    BeanUtils.copyProperties(analysis, analysisPDto);
    analysisPDto.setUbsp(null);
    List<AnalysisPSessionDto> analysisPSessionDtoList = new ArrayList<AnalysisPSessionDto>();
    for (AnalysisSession analysisSession : analysis.getAnalysisSessions()) {
      AnalysisPSessionDto analysisPSessionDto = new AnalysisPSessionDto();
      BeanUtils.copyProperties(analysisSession, analysisPSessionDto);
      analysisPSessionDto.setNumber(analysisSession.getId().getNumber());
      analysisPSessionDtoList.add(analysisPSessionDto);
    }
    analysisPDto.setAnalysisPSessionDtoList(analysisPSessionDtoList);
    return analysisPDto;
  }

  public EnumResponse modifyP(AnalysisPDto analysisPDto) {
    String bucketName = "soccerbee-" + deployType;
    String ubsPath = "gpsdata/" + analysisPDto.getUbsp().replace("ubsp", "ubs");
    String ubspPath = "gpsdata/" + analysisPDto.getUbsp();
    if (AmazonS3Util.getS3ObjectList(bucketName, ubspPath).size() == 0)
      throw new LogicException(LogicErrorList.DoesNotExist_Ubsp);
    Analysis analysis = analysisRepo.findById(analysisPDto.getUbsp())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Ubsp));

    BeanUtils.copyProperties(analysisPDto, analysis);
    analysisRepo.save(analysis);
    analysisSessionRepo.deleteAll(analysis.getAnalysisSessions());
    for (AnalysisPSessionDto analysisPSessionDto : analysisPDto.getAnalysisPSessionDtoList()) {
      AnalysisSessionPK id = new AnalysisSessionPK();
      id.setUbsp(analysisPDto.getUbsp());
      id.setNumber(analysisPSessionDto.getNumber());
      AnalysisSession analysisSession = new AnalysisSession();
      analysisSession.setId(id);
      BeanUtils.copyProperties(analysisPSessionDto, analysisSession);
      analysisSessionRepo.save(analysisSession);
    }
    return EnumResponse.Modified;
  }

  public EnumResponse savePVisualizer(PVisualizerDto pVisualizerDto) {
    Analysis analysis = analysisRepo.findById(pVisualizerDto.getUbsp())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Ubsp));

    hitmapRepo.deleteByAnalysisSessionIdUbsp(analysis.getUbsp());
    for (HitmapDto hitmapDto : pVisualizerDto.getHitmapDtoList()) {
      Hitmap hitmap = new Hitmap();
      AnalysisSessionPK id = new AnalysisSessionPK();
      id.setUbsp(analysis.getUbsp());
      id.setNumber(hitmapDto.getNumber());
      AnalysisSession analysisSession = new AnalysisSession();
      analysisSession.setId(id);
      BeanUtils.copyProperties(hitmapDto, hitmap);
      hitmap.setAnalysisSession(analysisSession);
      hitmapRepo.save(hitmap);
    }

    sprintRepo.deleteByAnalysisSessionIdUbsp(analysis.getUbsp());
    for (SprintDto sprintDto : pVisualizerDto.getSprintDtoList()) {
      Sprint sprint = new Sprint();
      AnalysisSessionPK id = new AnalysisSessionPK();
      id.setUbsp(analysis.getUbsp());
      id.setNumber(sprintDto.getNumber());
      AnalysisSession analysisSession = new AnalysisSession();
      analysisSession.setId(id);
      BeanUtils.copyProperties(sprintDto, sprint);
      sprint.setAnalysisSession(analysisSession);
      sprintRepo.save(sprint);
    }
    return EnumResponse.Saved;
  }

  public PVisualizerDto getPVisualizer(String ubsp) {
    analysisRepo.findById(ubsp)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Ubsp));

    PVisualizerDto pVisualizerDto = new PVisualizerDto();
    List<HitmapDto> hitmapDtoList = new ArrayList<HitmapDto>();
    List<Hitmap> hitmapList = hitmapRepo.findByAnalysisSessionIdUbsp(ubsp);
    for (Hitmap hitmap : hitmapList) {
      HitmapDto hitmapDto = new HitmapDto();
      BeanUtils.copyProperties(hitmap, hitmapDto);
      hitmapDto.setNumber(hitmap.getAnalysisSession().getId().getNumber());
      hitmapDtoList.add(hitmapDto);
    }
    List<SprintDto> sprintDtoList = new ArrayList<SprintDto>();
    List<Sprint> sprintList = sprintRepo.findByAnalysisSessionIdUbsp(ubsp);
    for (Sprint sprint : sprintList) {
      SprintDto sprintDto = new SprintDto();
      BeanUtils.copyProperties(sprint, sprintDto);
      sprintDto.setNumber(sprint.getAnalysisSession().getId().getNumber());
      sprintDtoList.add(sprintDto);
    }

    pVisualizerDto.setHitmapDtoList(hitmapDtoList);
    pVisualizerDto.setSprintDtoList(sprintDtoList);
    return pVisualizerDto;
  }

  public EnumResponse savePStats(List<PStatsDto> pStatsDtoList) {
    for (PStatsDto pStatsDto : pStatsDtoList) {
      AnalysisSessionPK id = new AnalysisSessionPK();
      id.setUbsp(pStatsDto.getUbsp());
      id.setNumber(pStatsDto.getNumber());
      AnalysisSession analysisSession = analysisSessionRepo.findById(id)
          .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_AnalysisSession));
      analysisSession.setId(id);
      // dto 공유를 위한 기존 데이터 유지
      pStatsDto.setPosition(analysisSession.getPosition());
      BeanUtils.copyProperties(pStatsDto, analysisSession);
      analysisSessionRepo.save(analysisSession);
    }
    return EnumResponse.Saved;
  }


  public EnumResponse saveAbility(List<AbilityDto> abilityDtoList) {
    if (abilityDtoList.get(0).getNumberP() != null) {
      for (AbilityDto abilityDto : abilityDtoList) {
        Ability ability = new Ability();
        BeanUtils.copyProperties(abilityDto, ability);
        Account account = new Account();
        account.setIdfAccount(Integer.parseInt(abilityDto.getUbsp().split("_")[0]));
        ability.setAccount(account);
        AnalysisSessionPK id = new AnalysisSessionPK();
        id.setUbsp(abilityDto.getUbsp());
        id.setNumber(abilityDto.getNumberP());
        AnalysisSession analysisSession = new AnalysisSession();
        analysisSession.setId(id);
        ability.setAnalysisSession(analysisSession);
        abilityRepo.deleteAll(analysisSessionRepo.findById(id).get().getAbilities());
        abilityRepo.save(ability);
      }
    } else {
      for (AbilityDto abilityDto : abilityDtoList) {
        Account account = accountRepo.findById(abilityDto.getIdfAccount())
            .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));
        Ability ability = new Ability();
        BeanUtils.copyProperties(abilityDto, ability);
        account.setIdfAccount(abilityDto.getIdfAccount());
        ability.setAccount(account);

        MatchAnalysisSessionPK idMatchAnalysisSessionPK = new MatchAnalysisSessionPK();
        idMatchAnalysisSessionPK.setIdfMatch(abilityDto.getIdfMatch());
        idMatchAnalysisSessionPK.setNumber(abilityDto.getNumberT());
        MatchAnalysisSession matchAnalysisSession =
            matchAnalysisSessionRepo.findById(idMatchAnalysisSessionPK).orElseThrow(
                () -> new LogicException(LogicErrorList.DoesNotExist_MatchAnalysisSession));
        ability.setMatchAnalysisSession(matchAnalysisSession);
        abilityRepo.deleteAll(
            matchAnalysisSessionRepo.findById(idMatchAnalysisSessionPK).get().getAbilities());
        abilityRepo.save(ability);

        // 분석된 선수의 상태값 변경
        MatchAnalysisAccountPK idMatchAnalysisAccountPK = new MatchAnalysisAccountPK();
        idMatchAnalysisAccountPK.setIdfMatch(abilityDto.getIdfMatch());
        idMatchAnalysisAccountPK.setIdfAccount(abilityDto.getIdfAccount());
        MatchAnalysisAccount matchAnalysisAccount =
            matchAnalysisAccountRepo.findById(idMatchAnalysisAccountPK).orElseThrow(
                () -> new LogicException(LogicErrorList.DoesNotExist_MatchAnalysisAccount));
        matchAnalysisAccount.setAnalyzed(true);
        matchAnalysisAccountRepo.save(matchAnalysisAccount);
      }
    }
    return EnumResponse.Saved;
  }


  public List<PStatsDto> getPStatsList(String ubsp) {
    Analysis analysis = analysisRepo.findById(ubsp)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Analysis));

    List<PStatsDto> pStatsDtoList = new ArrayList<PStatsDto>();
    for (AnalysisSession analysisSession : analysis.getAnalysisSessions()) {
      PStatsDto pStatsDto = new PStatsDto();
      BeanUtils.copyProperties(analysisSession, pStatsDto);
      pStatsDto.setNumber(analysisSession.getId().getNumber());
      pStatsDtoList.add(pStatsDto);
    }
    return pStatsDtoList;
  }

  public EnumResponse submitUbst(UbstDto ubstDto) {
    Account account = accountRepo.findById(ubstDto.getIdfAccount())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));
    Match match = matchRepo.findById(ubstDto.getIdfMatch())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Match));
    TeamAccountPK id = new TeamAccountPK();
    id.setIdfTeam(match.getTeamOpponent().getTeam().getIdfTeam());
    id.setIdfAccount(account.getIdfAccount());
    teamAccountRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));
    String[] idfFromUbst = ubstDto.getUbst().split("\\_");
    if (!idfFromUbst[0].equals(ubstDto.getIdfMatch().toString())
        || !idfFromUbst[1].equals(ubstDto.getIdfAccount().toString()))
      throw new LogicException(LogicErrorList.NotMatched);

    // 분석된 선수의 상태값 변경
    MatchAnalysisAccountPK idMatchAnalysisAccountPK = new MatchAnalysisAccountPK();
    idMatchAnalysisAccountPK.setIdfMatch(ubstDto.getIdfMatch());
    idMatchAnalysisAccountPK.setIdfAccount(ubstDto.getIdfAccount());
    MatchAnalysisAccount matchAnalysisAccount = new MatchAnalysisAccount();
    matchAnalysisAccount.setId(idMatchAnalysisAccountPK);
    matchAnalysisAccount.setAnalyzed(false);
    matchAnalysisAccountRepo.save(matchAnalysisAccount);

    // 제출된 분석파일 push알람 요청
    List<TeamAccount> managerAccountList = match.getTeamOpponent().getTeam().getTeamAccounts()
        .stream().filter(at -> at.getGrade().equals("O") || at.getGrade().equals("A"))
        .collect(Collectors.toList());
    List<String> deviceTokenList = new ArrayList<String>();
    for (TeamAccount teamAccount : managerAccountList) {
      for (AccountDevice accountDevice : teamAccount.getAccount().getAccountDevices()) {
        deviceTokenList.add(accountDevice.getToken());
      }
    }

    // push 알람
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    fcmPush(account.getName() + "의 분석요청",
        df.format(match.getDate()) + "의 " + match.getTeamOpponent().getTeam().getName() + " VS "
            + match.getTeamOpponent().getName() + "의 매치에 해당 선수를 포함하여 다시 한번 분석해 주세요.",
        "submittedUbst", deviceTokenList);

    String bucketName = "soccerbee-" + deployType;
    String ubsPath = "gpsdata/" + ubstDto.getUbst().replace("ubst", "ubs").replace(
        ubstDto.getIdfMatch() + "_" + ubstDto.getIdfAccount(), ubstDto.getIdfAccount().toString());
    String ubstPath = "gpsdata/" + ubstDto.getUbst();
    AmazonS3Util.copyFile(bucketName, ubsPath, bucketName, ubstPath);
    AmazonS3Util.deleteFile(bucketName, ubsPath);

    return EnumResponse.Submitted;
  }

  private void fcmPush(String title, String message, String tag, List<String> deviceTokenList) {
    AndroidConfig androidConfig = AndroidConfig.builder().setTtl(Duration.ofMinutes(2).toMillis())
        .setCollapseKey(tag).setPriority(Priority.HIGH)
        .setNotification(AndroidNotification.builder().setTag(tag).build()).build();
    ApnsConfig apnsConfig = ApnsConfig.builder()
        .setAps(Aps.builder().setCategory(tag).setThreadId(tag).build()).build();

    MulticastMessage multiCast =
        MulticastMessage.builder().setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
            .setNotification(Notification.builder().setTitle(title).setBody(message).build())
            .addAllTokens(deviceTokenList).build();
    fcmUtil.sendAsync(multiCast);
  }

  public List<MatchDto> getMatchList(Integer idfTeam) {
    List<Match> matchList = matchRepo.findByTeamOpponentIdIdfTeam(idfTeam).stream()
        .filter(ml -> ml.getMatchAnalysis() == null).collect(Collectors.toList());
    List<MatchDto> matchDtoList = new ArrayList<MatchDto>();
    for (Match match : matchList) {
      String bucketName = "soccerbee-" + deployType;
      List<S3ObjectSummary> s3ObjectSummaryList =
          AmazonS3Util.getS3ObjectList(bucketName, "gpsdata/" + match.getIdfMatch()).stream()
              .filter(s3 -> s3.getKey().contains("ubst")).collect(Collectors.toList());
      if (s3ObjectSummaryList.size() > 0) {
        MatchDto matchDto = new MatchDto();
        BeanUtils.copyProperties(match, matchDto);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(match.getDate());
        matchDto.setDay(calendar.get(Calendar.DAY_OF_WEEK));
        matchDto.setEmblemTeam(match.getTeamOpponent().getTeam().getImageEmblem());
        matchDto.setEmblemOpponent(match.getTeamOpponent().getImage());
        matchDto.setNameOpponent(match.getTeamOpponent().getName());
        matchDto.setTotalUbst(s3ObjectSummaryList.size());
        matchDtoList.add(matchDto);
      }
    }
    return matchDtoList;
  }

  public MatchAnalysisDto getMatchInfo(Integer idfMatch) {
    Match match = matchRepo.findById(idfMatch)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Match));
    String bucketName = "soccerbee-" + deployType;
    Integer totalUbst = AmazonS3Util.getS3ObjectList(bucketName, "gpsdata/" + match.getIdfMatch())
        .stream().filter(s3 -> s3.getKey().contains("ubst")).collect(Collectors.toList()).size();
    if (totalUbst == 0)
      throw new LogicException(LogicErrorList.DoesNotExist_Ubst);

    MatchAnalysisDto matchAnalysisDto = new MatchAnalysisDto();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(match.getDate());
    matchAnalysisDto.setDay(calendar.get(Calendar.DAY_OF_WEEK));
    matchAnalysisDto.setNameTeam(match.getTeamOpponent().getTeam().getName());
    matchAnalysisDto.setEmblemTeam(match.getTeamOpponent().getTeam().getImageEmblem());
    matchAnalysisDto.setDate(match.getDate());
    matchAnalysisDto.setTotalUbst(totalUbst);
    matchAnalysisDto.setNameOpponent(match.getTeamOpponent().getName());
    matchAnalysisDto.setLocation(match.getLocation());

    if (match.getMatchAnalysis() == null) {
      matchAnalysisDto.setTotalPlayer(totalUbst);
      matchAnalysisDto.setTimeStart(match.getTimeStart());
      matchAnalysisDto.setTimeFinish(match.getTimeFinish());
      matchAnalysisDto.setSession(match.getSession());
      matchAnalysisDto.setAnalyzed(false);
    } else {
      matchAnalysisDto.setTotalPlayer(match.getMatchAnalysis().getMatchAnalysisAccounts().size());
      matchAnalysisDto.setTimeStart(match.getMatchAnalysis().getTimeStart());
      matchAnalysisDto.setTimeFinish(match.getMatchAnalysis().getTimeFinish());
      matchAnalysisDto.setSession(match.getMatchAnalysis().getSession());
      matchAnalysisDto.setAnalyzed(true);
    }
    return matchAnalysisDto;
  }

  public List<UbstDto> getUbstList(Integer idfMatch) {
    Match match = matchRepo.findById(idfMatch)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Match));
    String bucketName = "soccerbee-" + deployType;
    List<S3ObjectSummary> s3ObjectSummaryList =
        AmazonS3Util.getS3ObjectList(bucketName, "gpsdata/" + match.getIdfMatch()).stream()
            .filter(s3 -> s3.getKey().contains("ubst")).collect(Collectors.toList());
    if (s3ObjectSummaryList.size() == 0)
      throw new LogicException(LogicErrorList.DoesNotExist_Ubst);

    List<UbstDto> ubstDtoList = new ArrayList<UbstDto>();
    for (S3ObjectSummary s3ObjectSummary : s3ObjectSummaryList) {
      UbstDto ubstDto = new UbstDto();
      String[] s3Key = s3ObjectSummary.getKey().split("\\_");
      Account account = accountRepo.findById(Integer.parseInt(s3Key[1]))
          .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

      ubstDto.setIdfMatch(idfMatch);
      ubstDto.setIdfAccount(account.getIdfAccount());
      ubstDto.setImage(account.getAccountPlayer().getImage());
      ubstDto.setName(account.getName());
      ubstDto.setUbst(s3ObjectSummary.getKey().replace("gpsdata/", ""));
      String position = match.getTeamOpponent().getTeam().getTeamAccounts().stream()
          .filter(ta -> ta.getAccount().getIdfAccount() == Integer.parseInt(s3Key[1]))
          .collect(Collectors.toList()).get(0).getPosition();
      ubstDto.setPosition(position);
      ubstDtoList.add(ubstDto);
    }
    return ubstDtoList;
  }

  public List<AnalysisDto> getList(int idfAccount, String yearMonth) {
    Account account = accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    List<AnalysisDto> analysisDtoList = new ArrayList<AnalysisDto>();
    DateFormat df = new SimpleDateFormat("yyyyMM");
    for (Analysis analysis : account.getAnalysis()) {
      if (df.format(analysis.getAnalysisSessions().get(0).getTimeStart()).equals(yearMonth)) {
        AnalysisDto analysisDto = new AnalysisDto();
        analysisDto.setDate(analysis.getAnalysisSessions().get(0).getTimeStart());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(analysisDto.getDate());
        analysisDto.setDay(calendar.get(Calendar.DAY_OF_WEEK));
        analysisDto.setName(analysis.getAccount().getName());
        analysisDto.setUbsp(analysis.getUbsp());
        BigDecimal rate = BigDecimal.valueOf(
            analysis.getAnalysisSessions().stream().filter(as -> Objects.nonNull(as.getRate()))
                .collect(Collectors.averagingDouble(as -> as.getRate().doubleValue())));
        analysisDto.setRate(rate);
        analysisDtoList.add(analysisDto);
      }
    }
    return analysisDtoList;
  }

  public List<TLineupDto> getTMatchLineupList(int idfMatch) {
    Match match = matchRepo.findById(idfMatch)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Match));

    List<TLineupDto> tLineupList = new ArrayList<TLineupDto>();
    for (Formation formation : formationRepo.findByIdIdfMatch(match.getIdfMatch())) {
      Account account = accountRepo.findById(formation.getId().getIdfAccount())
          .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));
      TLineupDto tLineupDto = new TLineupDto();
      BeanUtils.copyProperties(formation, tLineupDto);
      tLineupDto.setIdfAccount(account.getIdfAccount());
      tLineupDto.setName(account.getName());
      tLineupDto.setImage(account.getAccountPlayer().getImage());
      tLineupDto.setSession(formation.getId().getSessionNumber());
      String bucketName = "soccerbee-" + deployType;
      List<S3ObjectSummary> s3ObjectSummaryList = AmazonS3Util
          .getS3ObjectList(bucketName, "gpsdata/" + idfMatch + "_" + tLineupDto.getIdfAccount())
          .stream().collect(Collectors.toList());
      if (s3ObjectSummaryList.size() != 0)
        tLineupDto.setHasUbst(true);
      tLineupList.add(tLineupDto);
    }
    return tLineupList;
  }

  public List<TLineupDto> getTAnalysisLineupList(int idfMatch) {
    MatchAnalysis matchAnalysis = matchAnalysisRepo.findById(idfMatch)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_MatchAnalysis));

    List<TLineupDto> tLineupList = new ArrayList<TLineupDto>();
    for (MatchAnalysisSessionFormation matchAnalysisSessionFormation : matchAnalysisSessionFormationRepo
        .findByIdIdfMatch(matchAnalysis.getIdfMatch())) {
      Account account = accountRepo.findById(matchAnalysisSessionFormation.getId().getIdfAccount())
          .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));
      TLineupDto tLineupDto = new TLineupDto();
      BeanUtils.copyProperties(matchAnalysisSessionFormation, tLineupDto);
      tLineupDto.setIdfAccount(account.getIdfAccount());
      tLineupDto.setName(account.getName());
      tLineupDto.setImage(account.getAccountPlayer().getImage());
      tLineupDto
          .setSession(matchAnalysisSessionFormation.getMatchAnalysisSession().getId().getNumber());
      String bucketName = "soccerbee-" + deployType;
      List<S3ObjectSummary> s3ObjectSummaryList = AmazonS3Util
          .getS3ObjectList(bucketName, "gpsdata/" + idfMatch + "_" + tLineupDto.getIdfAccount())
          .stream().collect(Collectors.toList());
      if (s3ObjectSummaryList.size() != 0)
        tLineupDto.setHasUbst(true);
      tLineupList.add(tLineupDto);
    }
    return tLineupList;
  }

  public EnumResponse registT1(AnalysisTDto analysisTDto) {
    Match match = matchRepo.findById(analysisTDto.getIdfMatch())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Match));

    MatchAnalysis matchAnalysis = new MatchAnalysis();
    BeanUtils.copyProperties(analysisTDto, matchAnalysis);
    matchAnalysisRepo.save(matchAnalysis);
    for (AnalysisTSessionDto analysisTSessionDto : analysisTDto.getAnalysisTSessionDtoList()) {
      MatchAnalysisSession matchAnalysisSession = new MatchAnalysisSession();
      MatchAnalysisSessionPK idMatchAnalysisSessionPK = new MatchAnalysisSessionPK();
      idMatchAnalysisSessionPK.setIdfMatch(match.getIdfMatch());
      idMatchAnalysisSessionPK.setNumber(analysisTSessionDto.getNumber());
      matchAnalysisSession.setId(idMatchAnalysisSessionPK);
      BeanUtils.copyProperties(analysisTSessionDto, matchAnalysisSession);
      matchAnalysisSessionRepo.save(matchAnalysisSession);
    }

    List<Integer> idfAccountList = Arrays.asList(analysisTDto.getIdfAccount());
    for (Integer idfAccount : idfAccountList) {
      String bucketName = "soccerbee-" + deployType;
      Integer totalUbst = AmazonS3Util
          .getS3ObjectList(bucketName, "gpsdata/" + analysisTDto.getIdfMatch() + "_" + idfAccount)
          .stream().collect(Collectors.toList()).size();
      if (totalUbst == 0)
        throw new LogicException(LogicErrorList.DoesNotExist_Ubst);

      MatchAnalysisAccount matchAnalysisAccount = new MatchAnalysisAccount();
      MatchAnalysisAccountPK idMatchAnalysisAccountPK = new MatchAnalysisAccountPK();
      idMatchAnalysisAccountPK.setIdfMatch(match.getIdfMatch());
      idMatchAnalysisAccountPK.setIdfAccount(idfAccount);
      matchAnalysisAccount.setId(idMatchAnalysisAccountPK);
      matchAnalysisAccount.setAnalyzed(false);
      matchAnalysisAccountRepo.save(matchAnalysisAccount);
    }
    return EnumResponse.Registered;
  }

  public EnumResponse modifyT1(AnalysisTDto analysisTDto) {
    MatchAnalysis matchAnalysis = matchAnalysisRepo.findById(analysisTDto.getIdfMatch())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_MatchAnalysis));

    BeanUtils.copyProperties(analysisTDto, matchAnalysis);
    matchAnalysisRepo.save(matchAnalysis);

    matchAnalysisSessionRepo.deleteAll(matchAnalysis.getMatchAnalysisSessions());
    for (AnalysisTSessionDto analysisTSessionDto : analysisTDto.getAnalysisTSessionDtoList()) {
      MatchAnalysisSession matchAnalysisSession = new MatchAnalysisSession();
      MatchAnalysisSessionPK idMatchAnalysisSessionPK = new MatchAnalysisSessionPK();
      idMatchAnalysisSessionPK.setIdfMatch(matchAnalysis.getIdfMatch());
      idMatchAnalysisSessionPK.setNumber(analysisTSessionDto.getNumber());
      matchAnalysisSession.setId(idMatchAnalysisSessionPK);
      BeanUtils.copyProperties(analysisTSessionDto, matchAnalysisSession);
      matchAnalysisSessionRepo.save(matchAnalysisSession);
    }

    matchAnalysisAccountRepo.deleteAll(matchAnalysis.getMatchAnalysisAccounts());
    List<Integer> idfAccountList = Arrays.asList(analysisTDto.getIdfAccount());
    for (Integer idfAccount : idfAccountList) {
      String bucketName = "soccerbee-" + deployType;
      Integer totalUbst = AmazonS3Util
          .getS3ObjectList(bucketName, "gpsdata/" + analysisTDto.getIdfMatch() + "_" + idfAccount)
          .stream().collect(Collectors.toList()).size();
      if (totalUbst == 0)
        throw new LogicException(LogicErrorList.DoesNotExist_Ubst);

      MatchAnalysisAccount matchAnalysisAccount = new MatchAnalysisAccount();
      MatchAnalysisAccountPK idMatchAnalysisAccountPK = new MatchAnalysisAccountPK();
      idMatchAnalysisAccountPK.setIdfMatch(matchAnalysis.getIdfMatch());
      idMatchAnalysisAccountPK.setIdfAccount(idfAccount);
      matchAnalysisAccount.setId(idMatchAnalysisAccountPK);
      matchAnalysisAccount.setAnalyzed(false);
      matchAnalysisAccountRepo.save(matchAnalysisAccount);
    }
    return EnumResponse.Modified;
  }

  public EnumResponse registT2(List<AnalysisTFormationDto> analysisTFormationDtoList) {
    for (AnalysisTFormationDto analysisTFormationDto : analysisTFormationDtoList) {
      MatchAnalysisSessionFormation matchAnalysisSessionFormation =
          new MatchAnalysisSessionFormation();
      MatchAnalysisSessionFormationPK id = new MatchAnalysisSessionFormationPK();
      id.setIdfAccount(analysisTFormationDto.getIdfAccount());
      id.setIdfMatch(analysisTFormationDto.getIdfMatch());
      id.setNumber(analysisTFormationDto.getNumber());
      matchAnalysisSessionFormation.setId(id);
      BeanUtils.copyProperties(analysisTFormationDto, matchAnalysisSessionFormation);
      matchAnalysisSessionFormationRepo.save(matchAnalysisSessionFormation);
    }
    return EnumResponse.Registered;
  }

  public EnumResponse modifyT2(List<AnalysisTFormationDto> analysisTFormationDtoList) {
    matchAnalysisSessionFormationRepo.deleteAll(matchAnalysisSessionFormationRepo
        .findByIdIdfMatch(analysisTFormationDtoList.get(0).getIdfMatch()));

    for (AnalysisTFormationDto analysisTFormationDto : analysisTFormationDtoList) {
      MatchAnalysisSessionFormation matchAnalysisSessionFormation =
          new MatchAnalysisSessionFormation();
      MatchAnalysisSessionFormationPK id = new MatchAnalysisSessionFormationPK();
      id.setIdfAccount(analysisTFormationDto.getIdfAccount());
      id.setIdfMatch(analysisTFormationDto.getIdfMatch());
      id.setNumber(analysisTFormationDto.getNumber());
      matchAnalysisSessionFormation.setId(id);
      BeanUtils.copyProperties(analysisTFormationDto, matchAnalysisSessionFormation);
      matchAnalysisSessionFormationRepo.save(matchAnalysisSessionFormation);
    }
    return EnumResponse.Modified;
  }

  public EnumResponse saveTStats(List<TStatsDto> tStatsDtoList) {
    for (TStatsDto tStatsDto : tStatsDtoList) {
      MatchAnalysisSessionFormationPK id = new MatchAnalysisSessionFormationPK();
      id.setIdfMatch(tStatsDto.getIdfMatch());
      id.setIdfAccount(tStatsDto.getIdfAccount());
      id.setNumber(tStatsDto.getNumber());
      MatchAnalysisSessionFormation matchAnalysisSessionFormation =
          matchAnalysisSessionFormationRepo.findById(id).orElseThrow(
              () -> new LogicException(LogicErrorList.DoesNotExist_MatchAnalysisSessionFormation));
      tStatsDto.setPosition(matchAnalysisSessionFormation.getPosition());
      BeanUtils.copyProperties(tStatsDto, matchAnalysisSessionFormation);
      matchAnalysisSessionFormationRepo.save(matchAnalysisSessionFormation);
    }
    return EnumResponse.Saved;
  }

  public EnumResponse saveTVisualizer(List<TVisualizerDto> tVisualizerDtoList) {
    for (TVisualizerDto tVisualizerDto : tVisualizerDtoList) {
      MatchAnalysisAccountPK idMatchAnalysisAccountPK = new MatchAnalysisAccountPK();
      idMatchAnalysisAccountPK.setIdfMatch(tVisualizerDto.getIdfMatch());
      idMatchAnalysisAccountPK.setIdfAccount(tVisualizerDto.getIdfAccount());
      matchAnalysisAccountRepo.findById(idMatchAnalysisAccountPK)
          .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_MatchAnalysisAccount));

      matchHitmapRepo
          .deleteByMatchAnalysisSessionFormationIdIdfMatchAndMatchAnalysisSessionFormationIdIdfAccount(
              tVisualizerDto.getIdfMatch(), tVisualizerDto.getIdfAccount());
      for (HitmapDto hitmapDto : tVisualizerDto.getHitmapDtoList()) {
        MatchHitmap matchHitmap = new MatchHitmap();
        MatchAnalysisSessionFormationPK id = new MatchAnalysisSessionFormationPK();
        id.setIdfMatch(tVisualizerDto.getIdfMatch());
        id.setNumber(hitmapDto.getNumber());
        id.setIdfAccount(tVisualizerDto.getIdfAccount());
        MatchAnalysisSessionFormation matchAnalysisSessionFormation =
            matchAnalysisSessionFormationRepo.findById(id).orElseThrow(() -> new LogicException(
                LogicErrorList.DoesNotExist_MatchAnalysisSessionFormation));
        matchHitmap.setMatchAnalysisSessionFormation(matchAnalysisSessionFormation);
        BeanUtils.copyProperties(hitmapDto, matchHitmap);
        matchHitmapRepo.save(matchHitmap);
      }

      matchSprintRepo
          .deleteByMatchAnalysisSessionFormationIdIdfMatchAndMatchAnalysisSessionFormationIdIdfAccount(
              tVisualizerDto.getIdfMatch(), tVisualizerDto.getIdfAccount());
      for (SprintDto sprintDto : tVisualizerDto.getSprintDtoList()) {
        MatchSprint matchSprint = new MatchSprint();
        MatchAnalysisSessionFormationPK id = new MatchAnalysisSessionFormationPK();
        id.setIdfMatch(tVisualizerDto.getIdfMatch());
        id.setNumber(sprintDto.getNumber());
        id.setIdfAccount(tVisualizerDto.getIdfAccount());
        MatchAnalysisSessionFormation matchAnalysisSessionFormation =
            matchAnalysisSessionFormationRepo.findById(id).orElseThrow(() -> new LogicException(
                LogicErrorList.DoesNotExist_MatchAnalysisSessionFormation));
        matchSprint.setMatchAnalysisSessionFormation(matchAnalysisSessionFormation);
        BeanUtils.copyProperties(sprintDto, matchSprint);
        matchSprintRepo.save(matchSprint);
      }
    }
    return EnumResponse.Saved;
  }

  public List<TStatsDto> getTStatsList(Integer idfMatch, Integer idfAccount) {
    MatchAnalysisAccountPK id = new MatchAnalysisAccountPK();
    id.setIdfMatch(idfMatch);
    id.setIdfAccount(idfAccount);
    matchAnalysisAccountRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_MatchAnalysisAccount));

    List<TStatsDto> tStatsDtoList = new ArrayList<TStatsDto>();
    List<MatchAnalysisSessionFormation> matchAnalysisSessionFormationList =
        matchAnalysisSessionFormationRepo.findByIdIdfMatchAndIdIdfAccount(idfMatch, idfAccount);
    for (MatchAnalysisSessionFormation matchAnalysisSessionFormation : matchAnalysisSessionFormationList) {
      TStatsDto tStatsDto = new TStatsDto();
      BeanUtils.copyProperties(matchAnalysisSessionFormation, tStatsDto);
      tStatsDto.setNumber(matchAnalysisSessionFormation.getId().getNumber());
      tStatsDtoList.add(tStatsDto);
    }
    return tStatsDtoList;
  }

  public TVisualizerDto getTVisualizer(Integer idfMatch, Integer idfAccount) {
    MatchAnalysisAccountPK id = new MatchAnalysisAccountPK();
    id.setIdfMatch(idfMatch);
    id.setIdfAccount(idfAccount);
    MatchAnalysisAccount matchAnalysisAccount = matchAnalysisAccountRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_MatchAnalysisAccount));


    TVisualizerDto tVisualizerDto = new TVisualizerDto();
    tVisualizerDto.setName(matchAnalysisAccount.getAccount().getName());

    List<HitmapDto> hitmapDtoList = new ArrayList<HitmapDto>();
    List<MatchHitmap> matchHitmapList = matchHitmapRepo
        .findByMatchAnalysisSessionFormationIdIdfMatchAndMatchAnalysisSessionFormationIdIdfAccount(
            idfMatch, idfAccount);
    for (MatchHitmap matchHitmap : matchHitmapList) {
      HitmapDto hitmapDto = new HitmapDto();
      hitmapDto.setNumber(matchHitmap.getMatchAnalysisSessionFormation().getId().getNumber());
      BeanUtils.copyProperties(matchHitmap, hitmapDto);
      hitmapDtoList.add(hitmapDto);
    }

    List<SprintDto> sprintDtoList = new ArrayList<SprintDto>();
    List<MatchSprint> matchSprintList = matchSprintRepo
        .findByMatchAnalysisSessionFormationIdIdfMatchAndMatchAnalysisSessionFormationIdIdfAccount(
            idfMatch, idfAccount);
    for (MatchSprint matchSprint : matchSprintList) {
      SprintDto sprintDto = new SprintDto();
      sprintDto.setNumber(matchSprint.getMatchAnalysisSessionFormation().getId().getNumber());
      BeanUtils.copyProperties(matchSprint, sprintDto);
      sprintDtoList.add(sprintDto);
    }
    tVisualizerDto.setHitmapDtoList(hitmapDtoList);
    tVisualizerDto.setSprintDtoList(sprintDtoList);
    return tVisualizerDto;
  }

  public List<PitchPlayerDto> getPitchPlayerList(Integer idfMatch) {
    matchAnalysisRepo.findById(idfMatch)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_MatchAnalysis));
    List<MatchAnalysisSessionFormation> matchAnalysisSessionFormationList =
        matchAnalysisSessionFormationRepo.findByIdIdfMatch(idfMatch);

    List<PitchPlayerDto> pitchPlayerDtoList = new ArrayList<PitchPlayerDto>();
    for (MatchAnalysisSessionFormation matchAnalysisSessionFormation : matchAnalysisSessionFormationList) {
      PitchPlayerDto pitchPlayerDto = new PitchPlayerDto();
      BeanUtils.copyProperties(matchAnalysisSessionFormation, pitchPlayerDto);
      pitchPlayerDto.setIdfAccount(matchAnalysisSessionFormation.getAccount().getIdfAccount());
      pitchPlayerDto.setName(matchAnalysisSessionFormation.getAccount().getName());
      pitchPlayerDto.setNumber(matchAnalysisSessionFormation.getId().getNumber());
      pitchPlayerDtoList.add(pitchPlayerDto);
    }
    return pitchPlayerDtoList;
  }

  public EnumResponse saveTSessionStats(List<TSessionStatsDto> tSessionStatsDtoList) {
    for (TSessionStatsDto tSessionStatsDto : tSessionStatsDtoList) {
      MatchAnalysisSessionPK id = new MatchAnalysisSessionPK();
      id.setIdfMatch(tSessionStatsDto.getIdfMatch());
      id.setNumber(tSessionStatsDto.getNumber());
      MatchAnalysisSession matchAnalysisSession = matchAnalysisSessionRepo.findById(id)
          .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_MatchAnalysisSession));
      BeanUtils.copyProperties(tSessionStatsDto, matchAnalysisSession);
      matchAnalysisSessionRepo.save(matchAnalysisSession);
    }
    return EnumResponse.Saved;
  }

  public List<TSessionStatsDto> getTSessionStatsList(Integer idfMatch) {
    MatchAnalysis matchAnalysis = matchAnalysisRepo.findById(idfMatch)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_MatchAnalysis));

    List<TSessionStatsDto> tSessionStatsDtoList = new ArrayList<TSessionStatsDto>();
    for (MatchAnalysisSession matchAnalysisSession : matchAnalysis.getMatchAnalysisSessions()) {
      TSessionStatsDto tSessionStatsDto = new TSessionStatsDto();
      BeanUtils.copyProperties(matchAnalysisSession, tSessionStatsDto);
      tSessionStatsDto.setNumber(matchAnalysisSession.getId().getNumber());
      tSessionStatsDtoList.add(tSessionStatsDto);
    }
    return tSessionStatsDtoList;
  }

  public AnalysisTDto getTInfo(Integer idfMatch) {
    MatchAnalysis matchAnalysis = matchAnalysisRepo.findById(idfMatch)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_MatchAnalysis));

    AnalysisTDto analysisTDto = new AnalysisTDto();
    BeanUtils.copyProperties(matchAnalysis, analysisTDto);
    List<AnalysisTSessionDto> analysisTSessionDtoList = new ArrayList<AnalysisTSessionDto>();
    for (MatchAnalysisSession matchAnalysisSession : matchAnalysis.getMatchAnalysisSessions()) {
      AnalysisTSessionDto analysisTSessionDto = new AnalysisTSessionDto();
      BeanUtils.copyProperties(matchAnalysisSession, analysisTSessionDto);
      analysisTSessionDto.setNumber(matchAnalysisSession.getId().getNumber());
      analysisTSessionDtoList.add(analysisTSessionDto);
    }
    analysisTDto.setAnalysisTSessionDtoList(analysisTSessionDtoList);
    return analysisTDto;
  }
}
