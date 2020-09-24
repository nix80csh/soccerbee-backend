package com.soccerbee.api.locker;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LockerDto {
  @Getter
  @Setter
  public static class ScheduleDto {
    private Integer idfMatch;
    private Integer idfTeam;
    private Integer idfTeamOpponent;
    private String image;
    private String opponentName;
    private String opponentImage;
    private String location;
    @JsonFormat(pattern = "yyyyMMdd") private Date date;
    private int day;
    @JsonFormat(pattern = "HH:mm:ss") private Time timeStart;
    @JsonFormat(pattern = "HH:mm:ss") private Time timeFinish;
    private Boolean vote;
    private Boolean attendance;
    private Integer totalAttendance;
    private Boolean MyAnalysis;
    private Boolean teamAnalysis;
    private int analyzedTotalPlayer;
    private int nonAnalyzedTotalPlayer;
  }

  @Getter
  @Setter
  public static class TeamDto {
    private Integer idfAccount;
    private Integer idfTeam;
    private String imageEmblem;
    private String imageUniformHome;
    private String imageUniformAway;
    private String imageUniform3rd;
    private String name;
    private String nameAbbr;
    private String nameOwner;
    private String grade;
  }

  @Getter
  @Setter
  public static class RequestTeamDto {
    private Integer idfAccount;
    private Integer idfTeam;
    private String status;
  }

  @Getter
  @Setter
  public static class TeamSelectDto {
    private Integer idfAccount;
    private Integer idfTeam;
    private String name;
    private String image;
    private String grade;
    private Integer totalPlayer;
  }

  @Getter
  @Setter
  public static class VoteScheduleMatchDto {
    private Integer idfMatch;
    private Integer idfTeam;
    private Integer idfAccount;
    private Boolean vote;
  }

  @Getter
  @Setter
  public static class ComingUpMatchDto {
    private Integer idfMatch;
    private String nameTeam;
    private String nameOpponent;
    private String emblemTeam;
    private String emblemOpponent;
    @JsonFormat(pattern = "yyyyMMdd") private Date date;
    @JsonFormat(pattern = "HH:mm:ss") private Time timeStart;
    @JsonFormat(pattern = "HH:mm:ss") private Time timeFinish;
    private int day;
    private String location;
    private Integer idfTeam;
    private Boolean vote;
    private Integer totalVote;
    private Integer totalPlayer;
  }

  @Getter
  @Setter
  public static class PodUbsDto {
    private String ubsName;
    private Date uploadedDate;
  }

  @Getter
  @Setter
  public static class RecentAnalysisPDto {
    private String image;
    private String ubsp;
    private BigDecimal rate;
    private Integer day;
    private BigDecimal distance;
    private BigDecimal speedMax;
    private Integer sprint;
    private BigDecimal coverage;
  }

  @Getter
  @Setter
  public static class RecentAnalysisTDto {
    private Integer idfMatch;
    private Integer idfTeam;
    private String emblem;
    private String emblemOpponent;
    private Integer score;
    private Integer scoreOpponent;
    private String name;
    private String nameOpponent;
    private String location;
    @JsonFormat(pattern = "yyyyMMdd") private Date date;
    @JsonFormat(pattern = "HH:mm:ss") private Date timeStart;
    @JsonFormat(pattern = "HH:mm:ss") private Date timeFinish;
    private Integer day;
    private BigDecimal rate;
    private Boolean analysed;
  }

  @Getter
  @Setter
  public static class NoticeNewDto {
    private Integer idfNoticeNew;
    private Integer idfAdmin;
    private String title;
    private String image;
    private String content;
    @JsonFormat(pattern = "yyyyMMdd") private Date dueDate;
  }

  @Getter
  @Setter
  public static class NoticeSystemDto {
    private Integer idfNoticeSystem;
    private Integer idfAdmin;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyyMMdd") private Date regDate;
  }


  @Getter
  @Setter
  public static class WeatherDto {
    private Integer id;
    private String icon;
    private String type;
    private Integer humidity;
    private Double temperature;
    private Double windSpeed;
    private Double pop;
    private Double uvi;
  }
}
