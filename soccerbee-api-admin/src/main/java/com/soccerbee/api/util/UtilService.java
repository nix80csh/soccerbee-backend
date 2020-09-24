package com.soccerbee.api.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.soccerbee.api.util.UtilDto.CountryNameDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UtilService {
  public List<CountryNameDto> getCountryNameList(String langCode) {
    List<CountryNameDto> countryNameDtoList = new ArrayList<CountryNameDto>();
    String[] countries = Locale.getISOCountries();
    for (String country : countries) {
      CountryNameDto countryNameDto = new CountryNameDto();
      Locale locale = new Locale(langCode, country);
      countryNameDto.setCountryCode(locale.getCountry());
      countryNameDto.setCountryName(locale.getDisplayCountry(locale));
      countryNameDtoList.add(countryNameDto);
    }
    return countryNameDtoList;
  }
}
