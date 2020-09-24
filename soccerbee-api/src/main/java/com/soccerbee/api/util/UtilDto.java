package com.soccerbee.api.util;

import lombok.Getter;
import lombok.Setter;

public class UtilDto {
  @Getter
  @Setter
  public static class CountryDto {
    private String code;
    private String name;
  }

  @Getter
  @Setter
  public static class PositionDto {
    private String code;
    private String name;
  }

  @Getter
  @Setter
  public static class UniformDto {
    private String image;
  }

  @Getter
  @Setter
  public static class EmblemDto {
    private String image;
  }
}
