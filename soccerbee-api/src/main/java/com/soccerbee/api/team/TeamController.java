package com.soccerbee.api.team;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping("/team")
public class TeamController {
  @Autowired TeamService teamService;

  @GetMapping("/getInfo/{idfTeam}")
  public TeamDto getInfo(@PathVariable Integer idfTeam) {
    return teamService.getInfo(idfTeam);
  }

  @GetMapping("/getScheduleList/{idfTeam}/{yearMonth}")
  public List<ScheduleDto> getScheduleList(@PathVariable int idfTeam,
      @PathVariable String yearMonth) {
    return teamService.getScheduleList(idfTeam, yearMonth);
  }

  @DeleteMapping("/deleteMatch/{idfMatch}")
  public EnumResponse deleteMatch(@PathVariable int idfMatch) {
    return teamService.deleteMatch(idfMatch);
  }

  @PostMapping("/modifyAttendance")
  public EnumResponse modifyAttendance(@RequestBody ModifyAttendanceDto modifyAttendanceDto) {
    return teamService.modifyAttendance(modifyAttendanceDto);
  }

  @PostMapping("/registFormation")
  public EnumResponse registFormation(@RequestBody List<FormationDto> formationDtoList) {
    return teamService.registFormation(formationDtoList);
  }

  @GetMapping("/getFormationList/{idfMatch}/{idfTeam}")
  public List<FormationDto> getFormationList(@PathVariable int idfMatch,
      @PathVariable int idfTeam) {
    return teamService.getFormationList(idfMatch, idfTeam);
  }

  @GetMapping("/getMatchInfo/{idfMatch}")
  public MatchDto getMatchInfo(@PathVariable Integer idfMatch) {
    return teamService.getMatchInfo(idfMatch);
  }

  @GetMapping("/getMyFormationList/{idfMatch}/{idfTeam}/{idfAccount}")
  public List<MyFormationDto> getFormationList(@PathVariable int idfMatch,
      @PathVariable int idfTeam, @PathVariable int idfAccount) {
    return teamService.getMyFormationList(idfMatch, idfTeam, idfAccount);
  }

  @GetMapping("/getAttendanceList/{idfMatch}/{idfTeam}")
  public List<AttendanceDto> getAttendanceList(@PathVariable int idfMatch,
      @PathVariable int idfTeam) {
    return teamService.getAttendanceList(idfMatch, idfTeam);
  }

  @PostMapping("/registTimeline")
  public EnumResponse registTimeline(@RequestBody TimelineDto timelineDto) {
    return teamService.registTimeline(timelineDto);
  }

  @PostMapping("/modifyTimeline")
  public EnumResponse modifyTimeline(@RequestBody ModifyTimelineDto modifyTimelineDto) {
    return teamService.modifyTimeline(modifyTimelineDto);
  }

  @DeleteMapping("/deleteTimeLine")
  public EnumResponse deleteTimeLine(@RequestBody TimelineDto timelineDto) {
    return teamService.deleteTimeLine(timelineDto);
  }

  @DeleteMapping("/deleteTimeLineByType")
  public EnumResponse deleteTimeLineByType(@RequestBody TimelineDto timelineDto) {
    return teamService.deleteTimeLineByType(timelineDto);
  }

  @GetMapping("/getTimelineList/{idfMatch}")
  public List<TimelineDto> getTimelineList(@PathVariable int idfMatch) {
    return teamService.getTimelineList(idfMatch);
  }

  @GetMapping("/getTeamAuthGrade/{idfTeam}/{idfAccount}")
  public TeamAuthGradeDto getTeamAuthGrade(@PathVariable int idfTeam,
      @PathVariable int idfAccount) {
    return teamService.getTeamAuthGrade(idfTeam, idfAccount);
  }

  @PostMapping("/leave")
  public EnumResponse leave(@RequestBody LeaveDto leaveDto) {
    return teamService.leave(leaveDto);
  }
}
