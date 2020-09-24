package com.soccerbee.api.match;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.soccerbee.api.match.MatchDto.AttendeeDto;
import com.soccerbee.api.match.MatchDto.OpponentDto;
import com.soccerbee.common.EnumResponse;
import com.soccerbee.entity.Account;
import com.soccerbee.entity.AccountDevice;
import com.soccerbee.entity.Match;
import com.soccerbee.entity.ScheduleMatch;
import com.soccerbee.entity.ScheduleMatchPK;
import com.soccerbee.entity.Team;
import com.soccerbee.entity.TeamAccount;
import com.soccerbee.entity.TeamOpponent;
import com.soccerbee.entity.TeamOpponentPK;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AccountRepo;
import com.soccerbee.repo.MatchRepo;
import com.soccerbee.repo.ScheduleMatchRepo;
import com.soccerbee.repo.TeamOpponentRepo;
import com.soccerbee.repo.TeamRepo;
import com.soccerbee.util.FcmUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class MatchService {
  @Autowired AccountRepo accountRepo;
  @Autowired TeamOpponentRepo teamOpponentRepo;
  @Autowired TeamRepo teamRepo;
  @Autowired MatchRepo matchRepo;
  @Autowired ScheduleMatchRepo scheduleMatchRepo;
  @Autowired FcmUtil fcmUtil;

  public EnumResponse regist(MatchDto matchDto) {
    Account account = accountRepo.findById(matchDto.getIdfAccount())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));
    TeamOpponentPK idTeamOpponent = new TeamOpponentPK();
    idTeamOpponent.setIdfTeam(matchDto.getIdfTeam());
    idTeamOpponent.setIdfTeamOpponent(matchDto.getIdfTeamOpponent());
    TeamOpponent teamOpponent = teamOpponentRepo.findById(idTeamOpponent)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Opponent));

    Match match = new Match();
    BeanUtils.copyProperties(matchDto, match);
    match.setAccount(account);
    match.setTeamOpponent(teamOpponent);
    matchRepo.save(match);

    List<TeamAccount> filteredTeamAccountList =
        teamOpponent
            .getTeam().getTeamAccounts().stream().filter(at -> at.getGrade().equals("O")
                || at.getGrade().equals("M") || at.getGrade().equals("A"))
            .collect(Collectors.toList());

    List<String> deviceTokenList = new ArrayList<String>();
    for (TeamAccount teamAccount : filteredTeamAccountList) {
      ScheduleMatch scheduleMatch = new ScheduleMatch();
      ScheduleMatchPK idScheduleMatch = new ScheduleMatchPK();
      idScheduleMatch.setIdfAccount(teamAccount.getId().getIdfAccount());
      idScheduleMatch.setIdfTeam(matchDto.getIdfTeam());
      idScheduleMatch.setIdfMatch(match.getIdfMatch());
      scheduleMatch.setId(idScheduleMatch);
      scheduleMatch.setAttendance(false);
      scheduleMatchRepo.save(scheduleMatch);
      for (AccountDevice accountDevice : teamAccount.getAccount().getAccountDevices()) {
        deviceTokenList.add(accountDevice.getToken());
      }
    }
    fcmPush(match.getTeamOpponent().getTeam().getName(),
        matchDto.getPushAlarmTime() + " 새로운일정이  등록되었습니다. 출석체크를 해주세요.", "comingUpMatch",
        deviceTokenList);
    return EnumResponse.Registered;
  }

  public EnumResponse modify(MatchDto matchDto) {
    Account account = accountRepo.findById(matchDto.getIdfAccount())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));
    TeamOpponentPK idTeamOpponent = new TeamOpponentPK();
    idTeamOpponent.setIdfTeam(matchDto.getIdfTeam());
    idTeamOpponent.setIdfTeamOpponent(matchDto.getIdfTeamOpponent());
    TeamOpponent teamOpponent = teamOpponentRepo.findById(idTeamOpponent)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Opponent));
    Match match = matchRepo.findById(matchDto.getIdfMatch())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Match));

    match.setAccount(account);
    match.setTeamOpponent(teamOpponent);
    BeanUtils.copyProperties(matchDto, match);
    matchRepo.save(match);

    List<TeamAccount> filteredTeamAccountList =
        teamOpponent
            .getTeam().getTeamAccounts().stream().filter(at -> at.getGrade().equals("O")
                || at.getGrade().equals("M") || at.getGrade().equals("A"))
            .collect(Collectors.toList());
    for (TeamAccount teamAccount : filteredTeamAccountList) {
      ScheduleMatch scheduleMatch = new ScheduleMatch();
      ScheduleMatchPK idScheduleMatch = new ScheduleMatchPK();
      idScheduleMatch.setIdfAccount(teamAccount.getId().getIdfAccount());
      idScheduleMatch.setIdfTeam(matchDto.getIdfTeam());
      idScheduleMatch.setIdfMatch(match.getIdfMatch());
      scheduleMatch.setId(idScheduleMatch);
      scheduleMatch.setAttendance(false);
      scheduleMatchRepo.save(scheduleMatch);
    }
    return EnumResponse.Modified;
  }

  public EnumResponse registOpponent(OpponentDto opponentDto) {
    Team team = teamRepo.findById(opponentDto.getIdfTeam())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Team));
    TeamOpponent teamOpponent = new TeamOpponent();
    TeamOpponentPK id = new TeamOpponentPK();

    if (opponentDto.getIdfTeamOpponent() != null)
      id.setIdfTeamOpponent(opponentDto.getIdfTeamOpponent());

    id.setIdfTeam(team.getIdfTeam());
    teamOpponent.setId(id);
    teamOpponent.setVisible(true);
    BeanUtils.copyProperties(opponentDto, teamOpponent);
    teamOpponentRepo.save(teamOpponent);
    return EnumResponse.Registered;
  }

  public List<OpponentDto> getOpponentList(int idfTeam) {
    Team team = teamRepo.findById(idfTeam)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Team));
    List<OpponentDto> opponentDtoList = new ArrayList<OpponentDto>();
    List<TeamOpponent> filteredTeamOpponentList = team.getTeamOpponents().stream()
        .filter(to -> to.getVisible() == true).collect(Collectors.toList());

    for (TeamOpponent teamOpponent : filteredTeamOpponentList) {
      OpponentDto opponentDto = new OpponentDto();
      BeanUtils.copyProperties(teamOpponent, opponentDto);
      opponentDto.setIdfTeam(teamOpponent.getId().getIdfTeam());
      opponentDto.setIdfTeamOpponent(teamOpponent.getId().getIdfTeamOpponent());
      opponentDtoList.add(opponentDto);
    }
    return opponentDtoList;
  }

  public EnumResponse deleteOpponent(int idfTeam, int idfTeamOpponent) {
    TeamOpponentPK id = new TeamOpponentPK();
    id.setIdfTeam(idfTeam);
    id.setIdfTeamOpponent(idfTeamOpponent);
    TeamOpponent teamOpponent = teamOpponentRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Opponent));

    teamOpponent.setVisible(false);
    teamOpponentRepo.save(teamOpponent);
    return EnumResponse.Deleted;
  }

  public MatchDto getMatch(int idfMatch) {
    Match match = matchRepo.findById(idfMatch)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Match));
    MatchDto matchDto = new MatchDto();
    BeanUtils.copyProperties(match, matchDto);
    matchDto.setIdfAccount(match.getAccount().getIdfAccount());
    matchDto.setIdfTeam(match.getTeamOpponent().getId().getIdfTeam());
    matchDto.setIdfTeamOpponent(match.getTeamOpponent().getId().getIdfTeamOpponent());
    matchDto.setOpponentName(match.getTeamOpponent().getName());
    return matchDto;
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

  public List<AttendeeDto> getAttendeeList(int idfMatch) {
    Match match = matchRepo.findById(idfMatch)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Match));

    List<AttendeeDto> attendeeDtoList = new ArrayList<AttendeeDto>();
    for (ScheduleMatch scheduleMatch : match.getScheduleMatches()) {
      AttendeeDto attendeeDto = new AttendeeDto();
      attendeeDto.setIdfAccount(scheduleMatch.getId().getIdfAccount());
      attendeeDto.setName(scheduleMatch.getTeamAccount().getAccount().getName());
      attendeeDto
          .setImage(scheduleMatch.getTeamAccount().getAccount().getAccountPlayer().getImage());
      attendeeDto.setAttendance(scheduleMatch.getAttendance());
      attendeeDto.setVote(scheduleMatch.getVote());
      attendeeDtoList.add(attendeeDto);
    }
    return attendeeDtoList;
  }
}
