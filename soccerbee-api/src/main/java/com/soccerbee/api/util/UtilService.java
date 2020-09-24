package com.soccerbee.api.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soccerbee.api.util.UtilDto.CountryDto;
import com.soccerbee.api.util.UtilDto.EmblemDto;
import com.soccerbee.api.util.UtilDto.PositionDto;
import com.soccerbee.api.util.UtilDto.UniformDto;
import com.soccerbee.entity._Emblem;
import com.soccerbee.entity._Position;
import com.soccerbee.entity._Uniform;
import com.soccerbee.repo._EmblemRepo;
import com.soccerbee.repo._PositionRepo;
import com.soccerbee.repo._UniformRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UtilService {
  @Autowired _PositionRepo _positionRepo;
  @Autowired _UniformRepo _uniformRepo;
  @Autowired _EmblemRepo _emblemRepo;

  public List<CountryDto> getCountryList(String langCode) {
    List<CountryDto> countryDtoList = new ArrayList<CountryDto>();
    String[] countries = Locale.getISOCountries();
    for (String country : countries) {
      CountryDto countryDto = new CountryDto();
      Locale locale = new Locale(langCode, country);
      countryDto.setCode(locale.getCountry());
      countryDto.setName(locale.getDisplayCountry(locale));
      countryDtoList.add(countryDto);
    }
    return countryDtoList;
  }

  public List<PositionDto> getPositionList() {
    List<PositionDto> positionDtoList = new ArrayList<PositionDto>();
    List<_Position> positionList = _positionRepo.findAll();
    for (_Position _position : positionList) {
      PositionDto positionDto = new PositionDto();
      positionDto.setCode(_position.getCode());
      positionDto.setName(_position.getName());
      positionDtoList.add(positionDto);
    }
    return positionDtoList;
  }

  public List<UniformDto> getUniformList() {
    List<UniformDto> uniformDtoList = new ArrayList<UniformDto>();
    List<_Uniform> uniformList = _uniformRepo.findAll();
    for (_Uniform _uniform : uniformList) {
      UniformDto uniformDto = new UniformDto();
      BeanUtils.copyProperties(_uniform, uniformDto);
      uniformDtoList.add(uniformDto);
    }
    return uniformDtoList;
  }

  public List<EmblemDto> getEmblemList() {
    List<EmblemDto> emblemDtoList = new ArrayList<EmblemDto>();
    List<_Emblem> emblemList = _emblemRepo.findAll();
    for (_Emblem _emblem : emblemList) {
      EmblemDto emblemDto = new EmblemDto();
      BeanUtils.copyProperties(_emblem, emblemDto);
      emblemDtoList.add(emblemDto);
    }
    return emblemDtoList;
  }
}
