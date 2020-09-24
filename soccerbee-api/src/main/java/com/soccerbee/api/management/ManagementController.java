package com.soccerbee.api.management;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soccerbee.api.management.ManagementDto.AcceptRequestDto;
import com.soccerbee.api.management.ManagementDto.ChangeJerseyNumberDto;
import com.soccerbee.api.management.ManagementDto.ChangePositionDto;
import com.soccerbee.api.management.ManagementDto.DelegateOwnerDto;
import com.soccerbee.api.management.ManagementDto.ModifyGradeDto;
import com.soccerbee.api.management.ManagementDto.PlayerDto;
import com.soccerbee.api.management.ManagementDto.RequestedDto;
import com.soccerbee.common.EnumResponse;

@RestController
@RequestMapping("/management")
public class ManagementController {
  @Autowired ManagementService managementService;

  @GetMapping("/getRequestedList/{idfAccount}/{idfTeam}")
  public List<RequestedDto> getRequestedList(@PathVariable Integer idfAccount,
      @PathVariable Integer idfTeam) {
    return managementService.getRequestedList(idfAccount, idfTeam);
  }

  @GetMapping("/getPlayerList/{idfTeam}")
  public List<PlayerDto> getPlayerList(@PathVariable Integer idfTeam) {
    return managementService.getPlayerList(idfTeam);
  }

  @PostMapping("/delegateOwner")
  public EnumResponse delegateOwner(@RequestBody DelegateOwnerDto delegateOwnerDto) {
    return managementService.delegateOwner(delegateOwnerDto);
  }

  @PostMapping("/modifyGrade")
  public EnumResponse modifyGrade(@RequestBody ModifyGradeDto modifyGradeDto) {
    return managementService.modifyGrade(modifyGradeDto);
  }

  @DeleteMapping("/deletePlayer/{idfTeam}/{idfAccount}")
  public EnumResponse deletePlayer(@PathVariable int idfTeam, @PathVariable int idfAccount) {
    return managementService.deletePlayer(idfTeam, idfAccount);
  }

  @PostMapping("/acceptRequest")
  public EnumResponse acceptRequest(@RequestBody AcceptRequestDto acceptRequestDto) {
    return managementService.acceptRequest(acceptRequestDto);
  }

  @DeleteMapping("/deleteRequest/{idfTeam}/{idfAccount}")
  public EnumResponse deleteRequest(@PathVariable int idfTeam, @PathVariable int idfAccount) {
    return managementService.deleteRequest(idfTeam, idfAccount);
  }

  @PostMapping("/changeJerseyNumber")
  public EnumResponse changeJerseyNumber(@RequestBody ChangeJerseyNumberDto changeJerseyNumberDto) {
    return managementService.changeJerseyNumber(changeJerseyNumberDto);
  }

  @PostMapping("/changePosition")
  public EnumResponse changePosition(@RequestBody ChangePositionDto changePositionDto) {
    return managementService.changePosition(changePositionDto);
  }
}

