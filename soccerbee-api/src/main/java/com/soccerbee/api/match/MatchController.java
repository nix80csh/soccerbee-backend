package com.soccerbee.api.match;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soccerbee.api.match.MatchDto.AttendeeDto;
import com.soccerbee.api.match.MatchDto.OpponentDto;
import com.soccerbee.common.EnumResponse;

@RestController
@RequestMapping("/match")
public class MatchController {
  @Autowired MatchService scheduleService;

  @PostMapping("/regist")
  public EnumResponse regist(@RequestBody MatchDto matchDto) {
    return scheduleService.regist(matchDto);
  }

  @PostMapping("/modify")
  public EnumResponse modify(@RequestBody MatchDto matchDto) {
    return scheduleService.modify(matchDto);
  }

  @PostMapping("/registOpponent")
  public EnumResponse registOpponent(@RequestBody OpponentDto opponentDto) {
    return scheduleService.registOpponent(opponentDto);
  }

  @DeleteMapping("/deleteOpponent/{idfTeam}/{idfTeamOpponent}")
  public EnumResponse deleteOpponent(@PathVariable int idfTeam, @PathVariable int idfTeamOpponent) {
    return scheduleService.deleteOpponent(idfTeam, idfTeamOpponent);
  }

  @GetMapping("/getOpponentList/{idfTeam}")
  public List<OpponentDto> getOpponentList(@PathVariable int idfTeam) {
    return scheduleService.getOpponentList(idfTeam);
  }

  @GetMapping("/getMatch/{idfMatch}")
  public MatchDto getMatch(@PathVariable int idfMatch) {
    return scheduleService.getMatch(idfMatch);
  }

  @GetMapping("/getAttendeeList/{idfMatch}")
  public List<AttendeeDto> getAttendeeList(@PathVariable int idfMatch) {
    return scheduleService.getAttendeeList(idfMatch);
  }
}

