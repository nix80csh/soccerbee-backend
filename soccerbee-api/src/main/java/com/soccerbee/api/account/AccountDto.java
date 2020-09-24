package com.soccerbee.api.account;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {
  private Integer idfAccount;
  private String name;
  private String gender;
  @JsonFormat(pattern = "yyyyMMdd") private Date birthday;
  private Integer height;
  private Integer weight;
  private String position;
  private String foot;
  private String country;
  private String image;

  @Getter
  @Setter
  public static class PodDto {
    private String idfPod;
    private String type;
  }

  @Getter
  @Setter
  public static class ProfileImageDto {
    private int idfAccount;
    private MultipartFile fileImage;
    private String image;
  }

  @Getter
  @Setter
  public static class ModifyPasswordDto {
    private int idfAccount;
    private String oldPassword;
    private String newPassword;
  }

  @Getter
  @Setter
  public static class DeleteAccountDto {
    private int idfAccount;
    private String email;
    private String password;
  }

  @Getter
  @Setter
  public static class TeamOwnerDto {
    private Integer idfTeam;
    private String name;
  }
}
