package com.soccerbee.api.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soccerbee.api.util.UtilDto.CountryDto;
import com.soccerbee.api.util.UtilDto.EmblemDto;
import com.soccerbee.api.util.UtilDto.PositionDto;
import com.soccerbee.api.util.UtilDto.UniformDto;

@RestController
@RequestMapping("/util")
public class UtilController {
  @Autowired UtilService utilService;

  @GetMapping("/getCountryList/{langCode}")
  public List<CountryDto> getCountryList(@PathVariable String langCode) {
    return utilService.getCountryList(langCode);
  }

  @GetMapping("/getPositionList")
  public List<PositionDto> getPositionList() {
    return utilService.getPositionList();
  }

  @GetMapping("/getUniformList")
  public List<UniformDto> getUniformList() {
    return utilService.getUniformList();
  }

  @GetMapping("/getEmblemList")
  public List<EmblemDto> getEmblemList() {
    return utilService.getEmblemList();
  }
}
