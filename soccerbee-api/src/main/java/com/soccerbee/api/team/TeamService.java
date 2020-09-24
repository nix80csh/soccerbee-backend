package com.soccerbee.api.team;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soccerbee.api.team.TeamDto.AttendanceDto;
import com.soccerbee.api.team.TeamDto.FormationDto;
import com.soccerbee.api.team.TeamDto.LeaveDto;
import com.soccerbee.api.team.TeamDto.MatchDto;
import com.soccerbee.api.team.TeamDto.ModifyAttendanceDto;
import com.soccerbee.api.team.TeamDto.ModifyTimelineDto;
import com.soccerbee.api.team.TeamDto.MyFormationDto;
import com.soccerbee.api.team.TeamDto.ScheduleDto;
import com.soccerbee.api.team.TeamDto.TeamAuthGradeDto;
import com.soccerbee.api.team.TeamDto.TimelineDto;
import com.soccerbee.common.EnumResponse;
import com.soccerbee.entity.Account;
import com.soccerbee.entity.Formation;
import com.soccerbee.entity.FormationPK;
import com.soccerbee.entity.Match;
import com.soccerbee.entity.ScheduleMatch;
import com.soccerbee.entity.ScheduleMatchPK;
import com.soccerbee.entity.Team;
import com.soccerbee.entity.TeamAccount;
import com.soccerbee.entity.TeamAccountPK;
import com.soccerbee.entity.Timeline;
import com.soccerbee.entity.TimelinePK;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AccountRepo;
import com.soccerbee.repo.FormationRepo;
import com.soccerbee.repo.MatchAnalysisAccountRepo;
import com.soccerbee.repo.MatchRepo;
import com.soccerbee.repo.ScheduleMatchRepo;
import com.soccerbee.repo.TeamAccountRepo;
import com.soccerbee.repo.TeamRepo;
import com.soccerbee.repo.TimelineRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TeamService {
  @Autowired AccountRepo accountRepo;
  @Autowired TeamRepo teamRepo;
  @Autowired MatchRepo matchRepo;
  @Autowired MatchAnalysisAccountRepo matchAnalysisAccountRepo;
  @Autowired TeamAccountRepo teamAccountRepo;
  @Autowired ScheduleMatchRepo scheduleMatchRepo;
  @Autowired FormationRepo formationRepo;
  @Autowired TimelineRepo timelineRepo;

  public TeamDto getInfo(Integer idfTeam) {
    Team team = teamRepo.findById(idfTeam)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Team));

    TeamDto teamDto = new TeamDto();
    BeanUtils.copyProperties(team, teamDto);
    return teamDto;
  }

  public List<ScheduleDto> getScheduleList(int idfTeam, String yearMonth) {
    List<Match> matchList = matchRepo.findByTeamOpponentIdIdfTeam(idfTeam);
    DateFormat df = new SimpleDateFormat("yyyyMM");
    List<Match> filteredMatchList = matchList.stream()
        .filter(ml -> df.format(ml.getDate()).equals(yearMonth)).collect(Collectors.toList());
    List<ScheduleDto> scheduleDtoList = new ArrayList<ScheduleDto>();
    for (Match match : filteredMatchList) {
      ScheduleDto scheduleDto = new ScheduleDto();
      BeanUtils.copyProperties(match, scheduleDto);
      scheduleDto.setIdfMatch(match.getIdfMatch());
      scheduleDto.setIdfTeam(match.getTeamOpponent().getId().getIdfTeam());
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(scheduleDto.getDate());
      scheduleDto.setDay(calendar.get(Calendar.DAY_OF_WEEK));
      scheduleDto.setEmblem(match.getTeamOpponent().getTeam().getImageEmblem());
      scheduleDto.setOpponentName(match.getTeamOpponent().getName());
      scheduleDto.setOpponentEmblem(match.getTeamOpponent().getImage());

      int scoreHome = timelineRepo.findByIdIdfMatch(match.getIdfMatch()).stream()
          .filter(tl -> tl.getType().equals("GF")).collect(Collectors.toList()).size();
      scheduleDto.setScoreHome(scoreHome);
      int scoreAway = timelineRepo.findByIdIdfMatch(match.getIdfMatch()).stream()
          .filter(tl -> tl.getType().equals("GA")).collect(Collectors.toList()).size();
      scheduleDto.setScoreAway(scoreAway);

      scheduleDto.setTeamAnalysis(false);
      if (match.getMatchAnalysis() != null)
        scheduleDto.setTeamAnalysis(true);

      scheduleDto.setAnalyzedTotalPlayer(
          matchAnalysisAccountRepo.countByIdIdfMatchAndAnalyzed(match.getIdfMatch(), true));

      scheduleDtoList.add(scheduleDto);
    }
    return scheduleDtoList;
  }

  public EnumResponse deleteMatch(int idfMatch) {
    Match match = matchRepo.findById(idfMatch)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Match));

    matchRepo.delete(match);
    return EnumResponse.Deleted;
  }

  public EnumResponse modifyAttendance(ModifyAttendanceDto modifyAttendanceDto) {
    Match match = matchRepo.findById(modifyAttendanceDto.getIdfMatch())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Match));
    if (!scheduleMatchRepo.existsByIdIdfMatchAndIdIdfTeam(modifyAttendanceDto.getIdfMatch(),
        modifyAttendanceDto.getIdfTeam()))
      throw new LogicException(LogicErrorList.DoesNotExist_ScheduleMatch);

    for (int i = 0; i < modifyAttendanceDto.getIdfAccount().length; i++) {
      Account account = accountRepo.findById(modifyAttendanceDto.getIdfAccount()[i])
          .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));
      ScheduleMatch scheduleMatch = new ScheduleMatch();
      ScheduleMatchPK id = new ScheduleMatchPK();
      id.setIdfMatch(modifyAttendanceDto.getIdfMatch());
      id.setIdfTeam(modifyAttendanceDto.getIdfTeam());
      id.setIdfAccount(account.getIdfAccount());
      scheduleMatch.setId(id);
      scheduleMatch.setAttendance(modifyAttendanceDto.getAttendance());
      scheduleMatch.setVote(modifyAttendanceDto.getVote()[i]);
      scheduleMatchRepo.save(scheduleMatch);
    }

    List<ScheduleMatch> trueScheduleMatchList = scheduleMatchRepo
        .findByIdIdfMatchAndIdIdfTeam(modifyAttendanceDto.getIdfMatch(),
            modifyAttendanceDto.getIdfTeam())
        .stream().filter(sm -> sm.getAttendance() == true).collect(Collectors.toList());
    for (ScheduleMatch scheduleMatch : trueScheduleMatchList) {
      for (byte i = 1; i <= match.getSession(); i++) {
        FormationPK id = new FormationPK();
        id.setIdfMatch(scheduleMatch.getId().getIdfMatch());
        id.setIdfTeam(scheduleMatch.getId().getIdfTeam());
        id.setIdfAccount(scheduleMatch.getId().getIdfAccount());
        id.setSessionNumber(i);
        if (!formationRepo.existsById(id)) {
          Formation formation = new Formation();
          formation.setId(id);
          formation.setCaptain(false);
          formationRepo.save(formation);
        }
      }
    }

    List<ScheduleMatch> falseScheduleMatchList = scheduleMatchRepo
        .findByIdIdfMatchAndIdIdfTeam(modifyAttendanceDto.getIdfMatch(),
            modifyAttendanceDto.getIdfTeam())
        .stream().filter(sm -> sm.getAttendance() == false).collect(Collectors.toList());
    for (ScheduleMatch scheduleMatch : falseScheduleMatchList) {
      for (byte i = 1; i <= match.getSession(); i++) {
        FormationPK id = new FormationPK();
        id.setIdfMatch(scheduleMatch.getId().getIdfMatch());
        id.setIdfTeam(scheduleMatch.getId().getIdfTeam());
        id.setIdfAccount(scheduleMatch.getId().getIdfAccount());
        id.setSessionNumber(i);

        Formation formation = formationRepo.findById(id).orElse(null);
        if (formation != null && formation.getIdfAccountShift() != null) {
          FormationPK idShift = new FormationPK();
          idShift.setIdfMatch(scheduleMatch.getId().getIdfMatch());
          idShift.setIdfTeam(scheduleMatch.getId().getIdfTeam());
          idShift.setIdfAccount(formation.getIdfAccountShift());
          idShift.setSessionNumber(i);
          Formation shiftFormation = formationRepo.findById(idShift).orElse(null);
          if (shiftFormation != null) {
            shiftFormation.setReplacement(null);
            shiftFormation.setIdfAccountShift(null);
            formationRepo.save(shiftFormation);
          }
        }

        if (formation != null)
          formationRepo.delete(formation);
      }
    }
    return EnumResponse.Modified;
  }

  public EnumResponse registFormation(List<FormationDto> formationDtoList) {
    for (FormationDto formationDto : formationDtoList) {
      if (!scheduleMatchRepo.existsByIdIdfMatchAndIdIdfTeam(formationDto.getIdfMatch(),
          formationDto.getIdfTeam()))
        throw new LogicException(LogicErrorList.DoesNotExist_ScheduleMatch);

      FormationPK id = new FormationPK();
      id.setIdfMatch(formationDto.getIdfMatch());
      id.setIdfTeam(formationDto.getIdfTeam());
      id.setIdfAccount(formationDto.getIdfAccount());
      id.setSessionNumber(formationDto.getSessionNumber());
      Formation formation = new Formation();
      formation.setId(id);
      formation.setIdfAccountShift(formationDto.getIdfAccountShift());
      formation.setReplacement(formationDto.getReplacement());
      formation.setLocation(formationDto.getLocation());
      formation.setPosition(formationDto.getPosition());
      formation.setCaptain(formationDto.getCaptain());
      formationRepo.save(formation);
    }
    return EnumResponse.Registered;
  }

  public List<FormationDto> getFormationList(int idfMatch, int idfTeam) {
    if (!scheduleMatchRepo.existsByIdIdfMatchAndIdIdfTeam(idfMatch, idfTeam))
      throw new LogicException(LogicErrorList.DoesNotExist_ScheduleMatch);

    List<Formation> formationList = formationRepo.findByIdIdfMatchAndIdIdfTeam(idfMatch, idfTeam);
    List<FormationDto> formationDtoList = new ArrayList<FormationDto>();
    for (Formation formation : formationList) {
      FormationDto formationDto = new FormationDto();
      BeanUtils.copyProperties(formation, formationDto);
      formationDto.setIdfMatch(formation.getId().getIdfMatch());
      formationDto.setIdfTeam(formation.getId().getIdfTeam());
      formationDto.setIdfAccount(formation.getId().getIdfAccount());
      formationDto.setSessionNumber(formation.getId().getSessionNumber());
      formationDto.setName(formation.getScheduleMatch().getTeamAccount().getAccount().getName());
      formationDto.setImage(
          formation.getScheduleMatch().getTeamAccount().getAccount().getAccountPlayer().getImage());
      formationDtoList.add(formationDto);
    }
    return formationDtoList;
  }

  public MatchDto getMatchInfo(Integer idfMatch) {
    Match match = matchRepo.findById(idfMatch)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Match));

    MatchDto matchDto = new MatchDto();
    BeanUtils.copyProperties(match, matchDto);
    matchDto.setEmblem(match.getTeamOpponent().getTeam().getImageEmblem());
    matchDto.setName(match.getTeamOpponent().getTeam().getName());
    matchDto.setNameCoach(null);
    matchDto.setOpponentEmblem(match.getTeamOpponent().getImage());
    matchDto.setOpponentName(match.getTeamOpponent().getName());
    matchDto.setImageUniformHome(match.getTeamOpponent().getTeam().getImageUniformHome());
    matchDto.setImageUniformAway(match.getTeamOpponent().getTeam().getImageUniformAway());
    matchDto.setImageUniform3rd(match.getTeamOpponent().getTeam().getImageUniform3rd());
    return matchDto;
  }

  public List<MyFormationDto> getMyFormationList(int idfMatch, int idfTeam, int idfAccount) {
    List<Formation> formationList =
        formationRepo.findByIdIdfMatchAndIdIdfTeamAndIdIdfAccount(idfMatch, idfTeam, idfAccount);
    List<MyFormationDto> myFormationDtoList = new ArrayList<MyFormationDto>();
    for (Formation formation : formationList) {
      MyFormationDto myFormationDto = new MyFormationDto();
      BeanUtils.copyProperties(formation, myFormationDto);
      myFormationDto.setSessionNumber(formation.getId().getSessionNumber());
      myFormationDtoList.add(myFormationDto);
    }
    return myFormationDtoList;
  }

  public List<AttendanceDto> getAttendanceList(int idfMatch, int idfTeam) {
    if (!scheduleMatchRepo.existsByIdIdfMatchAndIdIdfTeam(idfMatch, idfTeam))
      throw new LogicException(LogicErrorList.DoesNotExist_ScheduleMatch);

    List<AttendanceDto> attendanceDtoList = new ArrayList<AttendanceDto>();
    List<ScheduleMatch> scheduleMatchList =
        scheduleMatchRepo.findByIdIdfMatchAndIdIdfTeam(idfMatch, idfTeam);
    for (ScheduleMatch scheduleMatch : scheduleMatchList) {
      AttendanceDto attendanceDto = new AttendanceDto();
      BeanUtils.copyProperties(scheduleMatch, attendanceDto);
      attendanceDto.setIdfAccount(scheduleMatch.getId().getIdfAccount());
      attendanceDto.setName(scheduleMatch.getTeamAccount().getAccount().getName());
      attendanceDto
          .setImage(scheduleMatch.getTeamAccount().getAccount().getAccountPlayer().getImage());
      attendanceDto.setPosition(scheduleMatch.getTeamAccount().getPosition());
      attendanceDtoList.add(attendanceDto);
    }
    return attendanceDtoList;
  }

  public EnumResponse registTimeline(TimelineDto timelineDto) {
    FormationPK idFormationPK = new FormationPK();
    idFormationPK.setIdfMatch(timelineDto.getIdfMatch());
    idFormationPK.setIdfTeam(timelineDto.getIdfTeam());
    idFormationPK.setIdfAccount(timelineDto.getIdfAccount());
    idFormationPK.setSessionNumber(timelineDto.getSessionNumber());
    Formation fotmation = formationRepo.findById(idFormationPK)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Formation));

    Timeline timeline = new Timeline();
    TimelinePK idTimelinePK = new TimelinePK();
    idTimelinePK.setIdfMatch(fotmation.getId().getIdfMatch());
    idTimelinePK.setIdfTeam(fotmation.getId().getIdfTeam());
    idTimelinePK.setIdfAccount(fotmation.getId().getIdfAccount());
    idTimelinePK.setSessionNumber((fotmation.getId().getSessionNumber()));
    idTimelinePK.setTimeline(new Date());
    timeline.setId(idTimelinePK);
    timeline.setType(timelineDto.getType());
    timelineRepo.save(timeline);
    return EnumResponse.Registered;
  }


  public EnumResponse modifyTimeline(ModifyTimelineDto modifyTimelineDto) {
    TimelinePK idTimelinePK = new TimelinePK();
    idTimelinePK.setIdfMatch(modifyTimelineDto.getIdfMatch());
    idTimelinePK.setIdfTeam(modifyTimelineDto.getIdfTeam());
    idTimelinePK.setIdfAccount(modifyTimelineDto.getIdfAccount());
    idTimelinePK.setSessionNumber((modifyTimelineDto.getSessionNumber()));
    idTimelinePK.setTimeline(modifyTimelineDto.getTimeline());
    Timeline timeline = timelineRepo.findById(idTimelinePK)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Timeline));

    FormationPK idFormationPK = new FormationPK();
    idFormationPK.setIdfMatch(modifyTimelineDto.getIdfMatch());
    idFormationPK.setIdfTeam(modifyTimelineDto.getIdfTeam());
    idFormationPK.setIdfAccount(modifyTimelineDto.getModifyIdfAccount());
    idFormationPK.setSessionNumber(modifyTimelineDto.getSessionNumber());
    formationRepo.findById(idFormationPK)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Formation));

    Timeline modyfyTimeline = new Timeline();
    TimelinePK idModifyTimelinePK = new TimelinePK();
    idModifyTimelinePK.setIdfMatch(modifyTimelineDto.getIdfMatch());
    idModifyTimelinePK.setIdfTeam(modifyTimelineDto.getIdfTeam());
    idModifyTimelinePK.setIdfAccount(modifyTimelineDto.getModifyIdfAccount());
    idModifyTimelinePK.setSessionNumber((modifyTimelineDto.getSessionNumber()));
    idModifyTimelinePK.setTimeline(modifyTimelineDto.getModifyTimeline());
    modyfyTimeline.setId(idModifyTimelinePK);
    modyfyTimeline.setType(timeline.getType());
    timelineRepo.save(modyfyTimeline);
    timelineRepo.delete(timeline);
    return EnumResponse.Modified;
  }

  public EnumResponse deleteTimeLine(TimelineDto timelineDto) {
    TimelinePK id = new TimelinePK();
    id.setIdfMatch(timelineDto.getIdfMatch());
    id.setIdfTeam(timelineDto.getIdfTeam());
    id.setIdfAccount(timelineDto.getIdfAccount());
    id.setSessionNumber(timelineDto.getSessionNumber());
    id.setTimeline(new Timestamp(timelineDto.getTimeline().getTime()));

    Timeline timeline = timelineRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Timeline));
    timelineRepo.delete(timeline);
    return EnumResponse.Deleted;
  }

  public EnumResponse deleteTimeLineByType(TimelineDto timelineDto) {
    List<Timeline> timelineList = timelineRepo
        .findByIdIdfMatchAndIdIdfTeamAndIdIdfAccountAndIdSessionNumber(timelineDto.getIdfMatch(),
            timelineDto.getIdfTeam(), timelineDto.getIdfAccount(), timelineDto.getSessionNumber());
    List<Timeline> filteredTimelineList = timelineList.stream()
        .filter(tl -> tl.getType().equals(timelineDto.getType())).collect(Collectors.toList());
    if (filteredTimelineList.size() <= 0)
      throw new LogicException(LogicErrorList.DoesNotExist_Timeline);

    Timeline timeline = filteredTimelineList.get(filteredTimelineList.size() - 1);
    timelineRepo.delete(timeline);
    return EnumResponse.Deleted;
  }

  public List<TimelineDto> getTimelineList(int idfMatch) {
    Match match = matchRepo.findById(idfMatch)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Match));

    List<TimelineDto> timelineDtoList = new ArrayList<TimelineDto>();
    List<Timeline> timelineList = timelineRepo.findByIdIdfMatch(idfMatch);
    for (Timeline timeline : timelineList) {
      TimelineDto timelineDto = new TimelineDto();
      timelineDto.setIdfMatch(timeline.getId().getIdfMatch());
      timelineDto.setIdfTeam(timeline.getId().getIdfTeam());
      timelineDto.setIdfAccount(timeline.getId().getIdfAccount());
      timelineDto.setSessionNumber(timeline.getId().getSessionNumber());
      timelineDto.setTimeline(timeline.getId().getTimeline());
      timelineDto.setType(timeline.getType());
      timelineDto.setName(
          timeline.getFormation().getScheduleMatch().getTeamAccount().getAccount().getName());
      timelineDto.setImage(timeline.getFormation().getScheduleMatch().getTeamAccount().getAccount()
          .getAccountPlayer().getImage());
      timelineDto.setEmblem(
          timeline.getFormation().getScheduleMatch().getTeamAccount().getTeam().getImageEmblem());
      timelineDtoList.add(timelineDto);
    }
    return timelineDtoList;
  }

  public TeamAuthGradeDto getTeamAuthGrade(int idfTeam, int idfAccount) {
    TeamAccountPK id = new TeamAccountPK();
    id.setIdfTeam(idfTeam);
    id.setIdfAccount(idfAccount);
    TeamAccount teamAccount = teamAccountRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));

    TeamAuthGradeDto teamAuthGradeDto = new TeamAuthGradeDto();
    teamAuthGradeDto.setGrade(teamAccount.getGrade());
    return teamAuthGradeDto;
  }

  public EnumResponse leave(LeaveDto leaveDto) {
    TeamAccountPK id = new TeamAccountPK();
    id.setIdfTeam(leaveDto.getIdfTeam());
    id.setIdfAccount(leaveDto.getIdfAccount());
    TeamAccount teamAccount = teamAccountRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));

    teamAccount.setGrade("L");
    teamAccountRepo.save(teamAccount);
    return EnumResponse.Left;
  }
}
