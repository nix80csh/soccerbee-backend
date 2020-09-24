package com.soccerbee.api.management;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagementDto {

  @Getter
  @Setter
  public static class RequestedDto {
    private Integer idfAccount;
    private String image;
    private String name;
    private String position;
    private String foot;
    private Integer height;
    private Integer weight;
  }

  @Getter
  @Setter
  public static class PlayerDto {
    private Integer idfAccount;
    private String image;
    private String name;
    private String position;
    private String grade;
    private Integer number;
  }

  @Getter
  @Setter
  public static class DelegateOwnerDto {
    private Integer idfTeam;
    private Integer idfAccount;
    private Integer idfAccountOwner;
  }
  @Getter
  @Setter
  public static class ModifyGradeDto {
    private Integer idfTeam;
    private Integer idfAccount;
  }

  @Getter
  @Setter
  public static class AcceptRequestDto {
    private Integer idfTeam;
    private Integer idfAccount;
  }

  @Getter
  @Setter
  public static class ChangeJerseyNumberDto {
    private Integer idfTeam;
    private Integer idfAccount;
    private Integer idfAccountTarget;
    private Integer number;
  }

  @Getter
  @Setter
  public static class ChangePositionDto {
    private Integer idfTeam;
    private Integer idfAccount;
    private String position;
  }
}
