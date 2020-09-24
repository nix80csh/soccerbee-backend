package com.soccerbee.api.match;

import java.sql.Time;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchDto {
  private Integer idfMatch;
  private Integer idfAccount;
  private Integer idfTeam;
  private Integer idfTeamOpponent;
  @JsonFormat(pattern = "yyyyMMdd") private Date date;
  @JsonFormat(pattern = "HH:mm:ss") private Time timeStart;
  @JsonFormat(pattern = "HH:mm:ss") private Time timeFinish;
  private String opponentName;
  private String latLong;
  private String location;
  private Integer session;
  private String pushAlarmTime;

  @Getter
  @Setter
  public static class OpponentDto {
    private Integer idfTeam;
    private Integer idfTeamOpponent;
    private String name;
    private String description;
    private String image;
  }

  @Getter
  @Setter
  public static class AttendeeDto {
    private Integer idfAccount;
    private String name;
    private String image;
    private Boolean vote;
    private Boolean attendance;
  }
}
