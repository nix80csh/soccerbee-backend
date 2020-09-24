package com.soccerbee.api.util;

import lombok.Getter;
import lombok.Setter;

public class UtilDto {
  @Getter
  @Setter
  public static class CountryNameDto {
    private String countryCode;
    private String countryName;
  }
}
