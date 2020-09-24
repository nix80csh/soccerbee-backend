package com.soccerbee.api.pod;

import java.sql.Time;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PodDto {
  private String idfPod;
  private String type;
  private String version;

  @Getter
  @Setter
  public static class TeamDto {
    private Integer idfTeam;
    private String name;
    private String nameAbbr;
    private String imageEmblem;
  }

  @Getter
  @Setter
  public static class AvailablePodDto {
    private String idfPod;
    private String type;
    private Integer idfAccount;
    private String name;
  }

  @Getter
  @Setter
  public static class PodOwnerDto {
    private String idfPod;
    private String type;
    private String email;
  }

  @Getter
  @Setter
  public static class UnanalyzedMatchDto {
    private Integer idfMatch;
    private Integer idfTeam;
    private int day;
    @JsonFormat(pattern = "yyyyMMdd") private Date date;
    private String emblem;
    private String emblemOpponent;
    private String name;
    private String nameAbbr;
    private String nameOpponent;
    private String location;
    @JsonFormat(pattern = "HH:mm:ss") private Time timeStart;
    @JsonFormat(pattern = "HH:mm:ss") private Time timeFinish;
  }
}
