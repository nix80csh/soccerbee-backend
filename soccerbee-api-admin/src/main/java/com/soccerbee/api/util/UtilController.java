package com.soccerbee.api.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soccerbee.api.util.UtilDto.CountryNameDto;

@RestController
@RequestMapping("/util")
public class UtilController {
  @Autowired UtilService utilService;

  @GetMapping("/getCountryNameList/{langCode}")
  public List<CountryNameDto> getCountryNameList(@PathVariable String langCode) {
    return utilService.getCountryNameList(langCode);
  }
}
