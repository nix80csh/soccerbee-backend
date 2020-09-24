package com.soccerbee.api.team;

import java.sql.Time;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDto {
  private Integer idfTeam;
  private String name;
  private String nameAbbr;
  private String imageEmblem;

  @Getter
  @Setter
  public static class ScheduleDto {
    private Integer idfMatch;
    private Integer idfTeam;
    private String emblem;
    private String opponentName;
    private String opponentEmblem;
    private String location;
    private int day;
    private int scoreHome;
    private int scoreAway;
    @JsonFormat(pattern = "yyyyMMdd") private Date date;
    @JsonFormat(pattern = "HH:mm:ss") private Time timeStart;
    @JsonFormat(pattern = "HH:mm:ss") private Time timeFinish;
    private Boolean teamAnalysis;
    private int analyzedTotalPlayer;
  }

  @Getter
  @Setter
  public static class ModifyAttendanceDto {
    private Integer idfMatch;
    private Integer idfTeam;
    private Integer[] idfAccount;
    private Boolean[] vote;
    private Boolean attendance;
  }

  @Getter
  @Setter
  public static class FormationDto {
    private Integer idfMatch;
    private Integer idfTeam;
    private Integer idfAccount;
    private Integer idfAccountShift;
    private Integer sessionNumber;
    private String replacement;
    private String position;
    private String location;
    private Boolean captain;
    private String name;
    private String image;
  }

  @Getter
  @Setter
  public static class MyFormationDto {
    private Integer sessionNumber;
    private String position;
  }

  @Getter
  @Setter
  public static class CaptainDto {
    private Integer idfMatch;
    private Integer idfTeam;
    private Integer idfAccount;
    private Integer sessionNumber;
  }

  @Getter
  @Setter
  public static class MatchDto {
    private String nameCoach;
    private String name;
    private String emblem;
    private String opponentName;
    private String opponentEmblem;
    private int session;
    private int day;
    @JsonFormat(pattern = "yyyyMMdd") private Date date;
    @JsonFormat(pattern = "HH:mm:ss") private Time timeStart;
    @JsonFormat(pattern = "HH:mm:ss") private Time timeFinish;
    private String latLong;
    private String location;
    private String imageUniformHome;
    private String imageUniformAway;
    private String imageUniform3rd;

  }

  @Getter
  @Setter
  public static class AttendanceDto {
    private Integer idfAccount;
    private String name;
    private String image;
    private String position;
    private Boolean vote;
    private boolean attendance;
  }

  @Getter
  @Setter
  public static class TimelineDto {
    private Integer idfMatch;
    private Integer idfTeam;
    private Integer idfAccount;
    private Integer sessionNumber;
    @JsonFormat(pattern = "yyyyMMdd HH:mm:ss") private Date timeline;
    private String type;
    private String name;
    private String image;
    private String emblem;
  }

  @Getter
  @Setter
  public static class ModifyTimelineDto {
    private Integer idfMatch;
    private Integer idfTeam;
    private Integer idfAccount;
    private Integer sessionNumber;
    @JsonFormat(pattern = "yyyyMMdd HH:mm:ss") private Date timeline;
    private Integer modifyIdfAccount;
    @JsonFormat(pattern = "yyyyMMdd HH:mm:ss") private Date modifyTimeline;
  }

  @Getter
  @Setter
  public static class TeamAuthGradeDto {
    private String grade;
  }

  @Getter
  @Setter
  public static class LeaveDto {
    private Integer idfTeam;
    private Integer idfAccount;
  }
}
