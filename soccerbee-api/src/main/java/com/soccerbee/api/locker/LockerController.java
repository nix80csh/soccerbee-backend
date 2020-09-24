package com.soccerbee.api.locker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping("/locker")
public class LockerController {
  @Autowired LockerService lockerService;

  @PostMapping("/registTeam")
  public EnumResponse registTeam(@RequestBody TeamDto teamDto) {
    return lockerService.registTeam(teamDto);
  }

  @PostMapping("/modifyTeam")
  public EnumResponse modifyTeam(@RequestBody TeamDto teamDto) {
    return lockerService.modifyTeam(teamDto);
  }

  @GetMapping("/searchTeam/{searchWord}/{idfAccount}")
  public List<TeamDto> searchTeam(@PathVariable String searchWord, @PathVariable int idfAccount) {
    return lockerService.searchTeam(searchWord, idfAccount);
  }

  @PostMapping("/requestTeam")
  public EnumResponse requestTeam(@RequestBody RequestTeamDto requestTeamDto) {
    return lockerService.requestTeam(requestTeamDto);
  }

  @PostMapping("/deleteRequestTeam")
  public EnumResponse deleteRequestTeam(@RequestBody RequestTeamDto requestTeamDto) {
    return lockerService.deleteRequestTeam(requestTeamDto);
  }

  @GetMapping("/getTeamSelectList/{idfAccount}")
  public List<TeamSelectDto> getTeamSelectList(@PathVariable int idfAccount) {
    return lockerService.getTeamSelectList(idfAccount);
  }

  @GetMapping("/getScheduleMatchList/{idfAccount}/{yearMonth}")
  public List<ScheduleDto> getScheduleMatchList(@PathVariable int idfAccount,
      @PathVariable String yearMonth) {
    return lockerService.getScheduleMatchList(idfAccount, yearMonth);
  }

  @PostMapping("/voteScheduleMatch")
  public EnumResponse voteScheduleMatch(@RequestBody VoteScheduleMatchDto voteScheduleMatchDto) {
    return lockerService.voteScheduleMatch(voteScheduleMatchDto);
  }

  @GetMapping("/getComingUpMatch/{idfAccount}")
  public ComingUpMatchDto getComingUpMatch(@PathVariable int idfAccount) {
    return lockerService.getComingUpMatch(idfAccount);
  }

  @GetMapping("/getPodUbsList/{idfAccount}")
  public List<PodUbsDto> getPodUbsList(@PathVariable Integer idfAccount) {
    return lockerService.getPodUbsList(idfAccount);
  }

  @GetMapping("/getTeam/{idfTeam}")
  public TeamDto getTeam(@PathVariable int idfTeam) {
    return lockerService.getTeam(idfTeam);
  }

  @GetMapping("/getRecentAnalysisP/{idfAccount}")
  public RecentAnalysisPDto getRecentAnalysisP(@PathVariable int idfAccount) {
    return lockerService.getRecentAnalysisP(idfAccount);
  }

  @GetMapping("/getRecentAnalysisT/{idfAccount}")
  public RecentAnalysisTDto getRecentAnalysisT(@PathVariable int idfAccount) {
    return lockerService.getRecentAnalysisT(idfAccount);
  }

  @GetMapping("/getNewsList")
  public List<NoticeNewDto> getNewsList() {
    return lockerService.getNewsList();
  }

  @GetMapping("/getNews/{idfNoticeNew}")
  public NoticeNewDto getNews(@PathVariable int idfNoticeNew) {
    return lockerService.getNews(idfNoticeNew);
  }

  @GetMapping("/getSystemList")
  public List<NoticeSystemDto> getSystemList() {
    return lockerService.getSystemList();
  }

  @GetMapping("/getSystem/{idfNoticeSystem}")
  public NoticeSystemDto getSystem(@PathVariable int idfNoticeSystem) {
    return lockerService.getSystem(idfNoticeSystem);
  }

  @GetMapping("/getMatchDayWeather/{idfMatch}")
  public WeatherDto getMatchDayWeather(@PathVariable int idfMatch) {
    return lockerService.getMatchDayWeather(idfMatch);
  }
}

