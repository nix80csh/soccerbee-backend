package com.soccerbee.api.analysis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soccerbee.api.analysis.AnalysisDto.AbilityDto;
import com.soccerbee.api.analysis.AnalysisDto.AnalysisPDto;
import com.soccerbee.api.analysis.AnalysisDto.AnalysisTDto;
import com.soccerbee.api.analysis.AnalysisDto.AnalysisTFormationDto;
import com.soccerbee.api.analysis.AnalysisDto.MatchAnalysisDto;
import com.soccerbee.api.analysis.AnalysisDto.MatchDto;
import com.soccerbee.api.analysis.AnalysisDto.PStatsDto;
import com.soccerbee.api.analysis.AnalysisDto.PVisualizerDto;
import com.soccerbee.api.analysis.AnalysisDto.PitchPlayerDto;
import com.soccerbee.api.analysis.AnalysisDto.TLineupDto;
import com.soccerbee.api.analysis.AnalysisDto.TSessionStatsDto;
import com.soccerbee.api.analysis.AnalysisDto.TStatsDto;
import com.soccerbee.api.analysis.AnalysisDto.TVisualizerDto;
import com.soccerbee.api.analysis.AnalysisDto.UbstDto;
import com.soccerbee.common.EnumResponse;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {
  @Autowired AnalysisService analysisService;

  @PostMapping("/registP")
  public EnumResponse registP(@RequestBody AnalysisPDto analysisPDto) {
    return analysisService.registP(analysisPDto);
  }

  @GetMapping("/getPInfo/{ubsp}")
  public AnalysisPDto getPInfo(@PathVariable String ubsp) {
    return analysisService.getPInfo(ubsp);
  }

  @PostMapping("/modifyP")
  public EnumResponse modifyP(@RequestBody AnalysisPDto analysisPDto) {
    return analysisService.modifyP(analysisPDto);
  }

  @PostMapping("/savePVisualizer")
  public EnumResponse savePVisualizer(@RequestBody PVisualizerDto pVisualizerDto) {
    return analysisService.savePVisualizer(pVisualizerDto);
  }

  @GetMapping("/getPVisualizer/{ubsp}")
  public PVisualizerDto getPVisualizer(@PathVariable String ubsp) {
    return analysisService.getPVisualizer(ubsp);
  }

  @PostMapping("/savePStats")
  public EnumResponse savePStats(@RequestBody List<PStatsDto> pStatsDtoList) {
    return analysisService.savePStats(pStatsDtoList);
  }

  @PostMapping("/saveAbility")
  public EnumResponse saveAbility(@RequestBody List<AbilityDto> abilityDtoList) {
    return analysisService.saveAbility(abilityDtoList);
  }

  @GetMapping("/getPStatsList/{ubsp}")
  public List<PStatsDto> getPStatsList(@PathVariable String ubsp) {
    return analysisService.getPStatsList(ubsp);
  }

  @PostMapping("/submitUbst")
  public EnumResponse submitUbst(@RequestBody UbstDto ubstDto) {
    return analysisService.submitUbst(ubstDto);
  }

  @GetMapping("/getMatchList/{idfTeam}")
  public List<MatchDto> getMatchList(@PathVariable Integer idfTeam) {
    return analysisService.getMatchList(idfTeam);
  }

  @GetMapping("/getMatchInfo/{idfMatch}")
  public MatchAnalysisDto getMatchInfo(@PathVariable Integer idfMatch) {
    return analysisService.getMatchInfo(idfMatch);
  }

  @GetMapping("/getUbstList/{idfMatch}")
  public List<UbstDto> getUbstList(@PathVariable Integer idfMatch) {
    return analysisService.getUbstList(idfMatch);
  }

  @GetMapping("/getList/{idfAccount}/{yearMonth}")
  public List<AnalysisDto> getList(@PathVariable int idfAccount, @PathVariable String yearMonth) {
    return analysisService.getList(idfAccount, yearMonth);
  }

  @GetMapping("/getTMatchLineupList/{idfMatch}")
  public List<TLineupDto> getTMatchLineupList(@PathVariable int idfMatch) {
    return analysisService.getTMatchLineupList(idfMatch);
  }

  @GetMapping("/getTAnalysisLineupList/{idfMatch}")
  public List<TLineupDto> getTAnalysisLineupList(@PathVariable int idfMatch) {
    return analysisService.getTAnalysisLineupList(idfMatch);
  }

  @PostMapping("/registT1")
  public EnumResponse registT1(@RequestBody AnalysisTDto analysisTDto) {
    return analysisService.registT1(analysisTDto);
  }

  @PostMapping("/modifyT1")
  public EnumResponse modifyT1(@RequestBody AnalysisTDto analysisTDto) {
    return analysisService.modifyT1(analysisTDto);
  }

  @PostMapping("/registT2")
  public EnumResponse registT2(@RequestBody List<AnalysisTFormationDto> analysisTFormationDtoList) {
    return analysisService.registT2(analysisTFormationDtoList);
  }

  @PostMapping("/modifyT2")
  public EnumResponse modifyT2(@RequestBody List<AnalysisTFormationDto> analysisTFormationDtoList) {
    return analysisService.modifyT2(analysisTFormationDtoList);
  }

  @PostMapping("/saveTStats")
  public EnumResponse saveTStats(@RequestBody List<TStatsDto> tStatsDtoList) {
    return analysisService.saveTStats(tStatsDtoList);
  }

  @PostMapping("/saveTVisualizer")
  public EnumResponse saveTVisualizer(@RequestBody List<TVisualizerDto> tVisualizerDtoList) {
    return analysisService.saveTVisualizer(tVisualizerDtoList);
  }

  @GetMapping("/getTStatsList/{idfMatch}/{idfAccount}")
  public List<TStatsDto> getTStatsList(@PathVariable Integer idfMatch,
      @PathVariable Integer idfAccount) {
    return analysisService.getTStatsList(idfMatch, idfAccount);
  }

  @GetMapping("/getTVisualizer/{idfMatch}/{idfAccount}")
  public TVisualizerDto getTVisualizer(@PathVariable Integer idfMatch,
      @PathVariable Integer idfAccount) {
    return analysisService.getTVisualizer(idfMatch, idfAccount);
  }

  @GetMapping("/getPitchPlayerList/{idfMatch}")
  public List<PitchPlayerDto> getPitchPlayerList(@PathVariable Integer idfMatch) {
    return analysisService.getPitchPlayerList(idfMatch);
  }

  @PostMapping("/saveTSessionStats")
  public EnumResponse saveTSessionStats(@RequestBody List<TSessionStatsDto> tSessionStatsDtoList) {
    return analysisService.saveTSessionStats(tSessionStatsDtoList);
  }

  @GetMapping("/getTSessionStatsList/{idfMatch}")
  public List<TSessionStatsDto> getTSessionStatsList(@PathVariable Integer idfMatch) {
    return analysisService.getTSessionStatsList(idfMatch);
  }

  @GetMapping("/getTInfo/{idfMatch}")
  public AnalysisTDto getTInfo(@PathVariable Integer idfMatch) {
    return analysisService.getTInfo(idfMatch);
  }
}

