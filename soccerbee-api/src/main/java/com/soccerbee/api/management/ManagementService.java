package com.soccerbee.api.management;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soccerbee.api.management.ManagementDto.AcceptRequestDto;
import com.soccerbee.api.management.ManagementDto.ChangeJerseyNumberDto;
import com.soccerbee.api.management.ManagementDto.ChangePositionDto;
import com.soccerbee.api.management.ManagementDto.DelegateOwnerDto;
import com.soccerbee.api.management.ManagementDto.ModifyGradeDto;
import com.soccerbee.api.management.ManagementDto.PlayerDto;
import com.soccerbee.api.management.ManagementDto.RequestedDto;
import com.soccerbee.common.EnumResponse;
import com.soccerbee.entity.Match;
import com.soccerbee.entity.ScheduleMatch;
import com.soccerbee.entity.ScheduleMatchPK;
import com.soccerbee.entity.Team;
import com.soccerbee.entity.TeamAccount;
import com.soccerbee.entity.TeamAccountPK;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.MatchRepo;
import com.soccerbee.repo.ScheduleMatchRepo;
import com.soccerbee.repo.TeamAccountRepo;
import com.soccerbee.repo.TeamRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ManagementService {
  @Autowired TeamAccountRepo teamAccountRepo;
  @Autowired TeamRepo teamRepo;
  @Autowired MatchRepo matchRepo;
  @Autowired ScheduleMatchRepo scheduleMatchRepo;

  public List<RequestedDto> getRequestedList(Integer idfAccount, Integer idfTeam) {
    TeamAccountPK id = new TeamAccountPK();
    id.setIdfAccount(idfAccount);
    id.setIdfTeam(idfTeam);

    TeamAccount teamAccount = teamAccountRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));

    if (!teamAccount.getGrade().equals("O") && !teamAccount.getGrade().equals("A"))
      throw new LogicException(LogicErrorList.Unauthorized);

    List<TeamAccount> teamAccountList = teamAccountRepo.findByIdIdfTeamAndGrade(idfTeam, "R");
    List<RequestedDto> requestedDtoList = new ArrayList<RequestedDto>();
    for (TeamAccount requestedTeamAccount : teamAccountList) {
      RequestedDto requestedDto = new RequestedDto();
      BeanUtils.copyProperties(requestedTeamAccount.getAccount().getAccountPlayer(), requestedDto);
      requestedDto.setName(requestedTeamAccount.getAccount().getName());
      requestedDto.setImage(requestedTeamAccount.getAccount().getAccountPlayer().getImage());
      requestedDtoList.add(requestedDto);
    }
    return requestedDtoList;
  }

  public List<PlayerDto> getPlayerList(Integer idfTeam) {
    Team team = teamRepo.findById(idfTeam)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Team));

    List<TeamAccount> teamAccountList = teamAccountRepo.findByIdIdfTeam(idfTeam);
    List<TeamAccount> filteredTeamAccountList =
        teamAccountList
            .stream().filter(Objects::nonNull).filter(at -> at.getGrade().equals("O")
                || at.getGrade().equals("M") || at.getGrade().equals("A"))
            .collect(Collectors.toList());

    List<PlayerDto> playerDtoList = new ArrayList<PlayerDto>();
    for (TeamAccount teamAccount : filteredTeamAccountList) {
      PlayerDto playerDto = new PlayerDto();
      BeanUtils.copyProperties(teamAccount, playerDto);
      playerDto.setIdfAccount(teamAccount.getAccount().getIdfAccount());
      playerDto.setName(teamAccount.getAccount().getName());
      playerDto.setImage(teamAccount.getAccount().getAccountPlayer().getImage());
      playerDtoList.add(playerDto);
    }
    return playerDtoList;
  }

  public EnumResponse delegateOwner(DelegateOwnerDto delegateOwnerDto) {
    TeamAccountPK id = new TeamAccountPK();
    id.setIdfAccount(delegateOwnerDto.getIdfAccount());
    id.setIdfTeam(delegateOwnerDto.getIdfTeam());
    TeamAccount teamAccount = teamAccountRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));

    if (!teamAccount.getGrade().equals("O"))
      throw new LogicException(LogicErrorList.NotMatched_GradeO);

    TeamAccountPK idOwner = new TeamAccountPK();
    idOwner.setIdfAccount(delegateOwnerDto.getIdfAccountOwner());
    idOwner.setIdfTeam(delegateOwnerDto.getIdfTeam());
    TeamAccount teamAccountOwner = teamAccountRepo.findById(idOwner)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));

    if (teamAccountOwner.getId().getIdfAccount() == teamAccount.getId().getIdfAccount())
      throw new LogicException(LogicErrorList.DuplicateProperty_IdfAccount);

    teamAccountOwner.setGrade("O");
    teamAccountRepo.save(teamAccountOwner);
    teamAccount.setGrade("M");
    teamAccountRepo.save(teamAccount);

    return EnumResponse.Delegated;
  }

  public EnumResponse modifyGrade(ModifyGradeDto modifyGradeDto) {
    TeamAccountPK id = new TeamAccountPK();
    id.setIdfAccount(modifyGradeDto.getIdfAccount());
    id.setIdfTeam(modifyGradeDto.getIdfTeam());
    TeamAccount teamAccount = teamAccountRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));

    if (teamAccount.getGrade().equals("M")) {
      teamAccount.setGrade("A");
    } else if (teamAccount.getGrade().equals("A")) {
      teamAccount.setGrade("M");
    }
    teamAccountRepo.save(teamAccount);
    return EnumResponse.Modified;
  }

  public EnumResponse deletePlayer(int idfTeam, int idfAccount) {
    TeamAccountPK id = new TeamAccountPK();
    id.setIdfTeam(idfTeam);
    id.setIdfAccount(idfAccount);
    TeamAccount teamAccount = teamAccountRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));

    teamAccountRepo.delete(teamAccount);
    return EnumResponse.Deleted;
  }

  public EnumResponse acceptRequest(AcceptRequestDto acceptRequestDto) {
    TeamAccountPK idTeamAccountPK = new TeamAccountPK();
    idTeamAccountPK.setIdfAccount(acceptRequestDto.getIdfAccount());
    idTeamAccountPK.setIdfTeam(acceptRequestDto.getIdfTeam());
    TeamAccount teamAccount = teamAccountRepo.findById(idTeamAccountPK)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));
    if (!teamAccount.getGrade().equals("R"))
      throw new LogicException(LogicErrorList.NotMatched);

    teamAccount.setGrade("M");
    teamAccountRepo.save(teamAccount);

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    List<Match> futureMatchList =
        matchRepo.findByTeamOpponentIdIdfTeam(teamAccount.getId().getIdfTeam()).stream()
            .filter(fm -> fm.getDate().after(calendar.getTime())).collect(Collectors.toList());
    for (Match match : futureMatchList) {
      ScheduleMatch scheduleMatch = new ScheduleMatch();
      ScheduleMatchPK idScheduleMatchPK = new ScheduleMatchPK();
      idScheduleMatchPK.setIdfAccount(teamAccount.getId().getIdfAccount());
      idScheduleMatchPK.setIdfTeam(teamAccount.getId().getIdfTeam());
      idScheduleMatchPK.setIdfMatch(match.getIdfMatch());
      scheduleMatch.setId(idScheduleMatchPK);
      scheduleMatch.setAttendance(false);
      scheduleMatchRepo.save(scheduleMatch);
    }
    return EnumResponse.Accepted;
  }

  public EnumResponse deleteRequest(int idfTeam, int idfAccount) {
    TeamAccountPK id = new TeamAccountPK();
    id.setIdfAccount(idfAccount);
    id.setIdfTeam(idfTeam);
    TeamAccount teamAccount = teamAccountRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));

    if (!teamAccount.getGrade().equals("R"))
      throw new LogicException(LogicErrorList.NotMatched_GradeR);

    teamAccountRepo.delete(teamAccount);
    return EnumResponse.Deleted;
  }

  public EnumResponse changeJerseyNumber(ChangeJerseyNumberDto changeJerseyNumberDto) {
    TeamAccountPK id = new TeamAccountPK();
    id.setIdfAccount(changeJerseyNumberDto.getIdfAccount());
    id.setIdfTeam(changeJerseyNumberDto.getIdfTeam());
    TeamAccount teamAccount = teamAccountRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));

    if (changeJerseyNumberDto.getIdfAccountTarget() != null) {
      TeamAccountPK targetId = new TeamAccountPK();
      targetId.setIdfAccount(changeJerseyNumberDto.getIdfAccountTarget());
      targetId.setIdfTeam(changeJerseyNumberDto.getIdfTeam());
      TeamAccount targetTeamAccount = teamAccountRepo.findById(targetId)
          .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));
      if (changeJerseyNumberDto.getNumber() != targetTeamAccount.getNumber())
        throw new LogicException(LogicErrorList.NotMatched_JerseyNumber);

      targetTeamAccount.setNumber(teamAccount.getNumber());
      teamAccountRepo.save(targetTeamAccount);
    }
    teamAccount.setNumber(changeJerseyNumberDto.getNumber());
    teamAccountRepo.save(teamAccount);
    return EnumResponse.Changed;
  }

  public EnumResponse changePosition(ChangePositionDto changePositionDto) {
    TeamAccountPK id = new TeamAccountPK();
    id.setIdfAccount(changePositionDto.getIdfAccount());
    id.setIdfTeam(changePositionDto.getIdfTeam());
    TeamAccount teamAccount = teamAccountRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_TeamAccount));

    teamAccount.setPosition(changePositionDto.getPosition());
    teamAccountRepo.save(teamAccount);
    return EnumResponse.Changed;
  }
}
