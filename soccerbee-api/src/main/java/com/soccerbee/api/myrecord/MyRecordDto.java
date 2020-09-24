package com.soccerbee.api.myrecord;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyRecordDto {


  @Getter
  @Setter
  public static class MyAbilityDto {
    private String name;
    private String image;
    private String position;
    private BigDecimal attack;
    private BigDecimal defense;
    private BigDecimal speed;
    private BigDecimal agility;
    private BigDecimal acceleration;
    private BigDecimal activity;
    private BigDecimal stamina;
    private BigDecimal condition;
    private BigDecimal teamwork;
  }


  @Getter
  @Setter
  public static class MyPositionDto {
    private String position;
    private BigDecimal rate;
  }
}
