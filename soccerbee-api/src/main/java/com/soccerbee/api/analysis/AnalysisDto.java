package com.soccerbee.api.analysis;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisDto {
  private String ubsp;
  private String name;
  private BigDecimal rate;
  private int day;
  @JsonFormat(pattern = "yyyyMMdd") private Date date;

  @Getter
  @Setter
  public static class AnalysisPDto {
    private String ubsp;
    private Integer idfAccount;
    private String image;
    private String podType;
    private Integer session;
    private String point;
    private String point1;
    private String point2;
    private String point3;
    private String point4;
    private List<AnalysisPSessionDto> analysisPSessionDtoList;
  }

  @Getter
  @Setter
  public static class AnalysisPSessionDto {
    private Integer number;
    private String position;
    @JsonFormat(pattern = "yyyyMMdd HH:mm:ss") private Date timeStart;
    @JsonFormat(pattern = "yyyyMMdd HH:mm:ss") private Date timeFinish;
  }

  @Getter
  @Setter
  public static class UbstDto {
    private Integer idfAccount;
    private Integer idfMatch;
    private String ubst;
    private String position;
    private String name;
    private String image;
  }

  @Getter
  @Setter
  public static class MatchDto {
    private Integer idfMatch;
    private int day;
    @JsonFormat(pattern = "yyyyMMdd") private Date date;
    @JsonFormat(pattern = "HH:mm:ss") private Time timeStart;
    @JsonFormat(pattern = "HH:mm:ss") private Time timeFinish;
    private String emblemTeam;
    private String emblemOpponent;
    private String nameTeam;
    private String nameOpponent;
    private String location;
    private Integer totalUbst;
  }

  @Getter
  @Setter
  public static class MatchAnalysisDto {
    private Integer idfMatch;
    private int day;
    @JsonFormat(pattern = "yyyyMMdd") private Date date;
    @JsonFormat(pattern = "HH:mm:ss") private Date timeStart;
    @JsonFormat(pattern = "HH:mm:ss") private Date timeFinish;
    private String emblemTeam;
    private String emblemOpponent;
    private String nameTeam;
    private String nameOpponent;
    private String location;
    private Integer session;
    private Integer totalUbst;
    private Integer totalPlayer;
    private boolean isAnalyzed;
  }

  @Getter
  @Setter
  public static class PVisualizerDto {
    private String ubsp;
    private String pitchX;
    private String pitchY;
    private List<HitmapDto> hitmapDtoList;
    private List<SprintDto> sprintDtoList;
    private List<PitchXYDto> pitchXYDtoList;
  }

  @Getter
  @Setter
  public static class HitmapDto {
    private Integer number;
    private Integer zone;
    private Integer frequency;
  }

  @Getter
  @Setter
  public static class SprintDto {
    private Integer number;
    private String pointStart;
    private String pointFinish;
    private Integer grade;
  }

  @Getter
  @Setter
  public static class PitchXYDto {
    private Integer number;
    private String pitchX;
    private String pitchY;
  }

  @Getter
  @Setter
  public static class PStatsDto {
    private String ubsp;
    private Integer number;
    private Integer duration;
    private BigDecimal rate;
    private BigDecimal distance;
    private BigDecimal speedMax;
    private BigDecimal speedAvg;
    private Integer sprint;
    private BigDecimal coverage;
    private String pitchX;
    private String pitchY;
    private String position;
  }

  @Getter
  @Setter
  public static class TLineupDto {
    private Integer idfAccount;
    private Integer idfAccountShift;
    private String replacement;
    private int session;
    private String image;
    private String name;
    private String position;
    private String location;
    private boolean hasUbst;
  }

  @Getter
  @Setter
  public static class AnalysisTDto {
    private Integer idfMatch;
    private Integer session;
    @JsonFormat(pattern = "yyyyMMdd HH:mm:ss") private Date timeStart;
    @JsonFormat(pattern = "yyyyMMdd HH:mm:ss") private Date timeFinish;
    private String point;
    private String point1;
    private String point2;
    private String point3;
    private String point4;
    private Integer[] idfAccount;
    private List<AnalysisTSessionDto> analysisTSessionDtoList;
  }

  @Getter
  @Setter
  public static class AnalysisTSessionDto {
    private Integer number;
    @JsonFormat(pattern = "yyyyMMdd HH:mm:ss") private Date timeStart;
    @JsonFormat(pattern = "yyyyMMdd HH:mm:ss") private Date timeFinish;
  }

  @Getter
  @Setter
  public static class AnalysisTFormationDto {
    private Integer idfMatch;
    private Integer number;
    private Integer idfAccount;
    private Integer idfAccountShift;
    private String replacement;
    private String position;
    private String location;
    private Boolean captain;
  }

  @Getter
  @Setter
  public static class AbilityDto {
    private Integer idfAccount;
    private String ubsp;
    private Integer numberP;
    private Integer idfMatch;
    private Integer numberT;
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
    private BigDecimal rate;
  }

  @Getter
  @Setter
  public static class TStatsDto {
    private Integer idfMatch;
    private Integer idfAccount;
    private Integer number;
    private Integer duration;
    private BigDecimal rate;
    private BigDecimal distance;
    private BigDecimal speedMax;
    private BigDecimal speedAvg;
    private Integer sprint;
    private BigDecimal coverage;
    private String pitchX;
    private String pitchY;
    private String position;
  }

  @Getter
  @Setter
  public static class TVisualizerDto {
    private Integer idfMatch;
    private Integer idfAccount;
    private String name;
    private List<HitmapDto> hitmapDtoList;
    private List<SprintDto> sprintDtoList;
  }

  @Getter
  @Setter
  public static class PitchPlayerDto {
    private Integer idfAccount;
    private String name;
    private Integer number;
    private String position;
    private String pitchX;
    private String pitchY;
  }

  @Getter
  @Setter
  public static class TSessionStatsDto {
    private Integer idfMatch;
    private Integer number;
    private String attackDirection;
    private BigDecimal attackRate;
    private BigDecimal zoneAttack;
    private BigDecimal zoneMidfield;
    private BigDecimal zoneDefense;
    private BigDecimal zoneRight;
    private BigDecimal zoneCenter;
    private BigDecimal zoneLeft;
    private String defenseLine;
  }
}
