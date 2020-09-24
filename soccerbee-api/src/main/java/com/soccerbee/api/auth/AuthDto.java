package com.soccerbee.api.auth;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDto {
  private Integer idfAccount;
  private String email;
  private String password;
  private Boolean agreement;
  private String name;
  @JsonFormat(pattern = "yyyyMMdd") private Date birthday;
  private String gender;
  private Integer height;
  private Integer weight;
  private String position;
  private String locale;
  private String idfDevice;
  private String osType;
  private String deviceToken;

  @Getter
  @Setter
  public static class SendAuthEmailDto {
    private int idfAccount;
    private String email;
    private String locale;
  }

  @Getter
  @Setter
  public static class SendPasswordDto {
    private String email;
    private String locale;
  }

  @Getter
  @Setter
  public static class AuthCodeDto {
    private String email;
    private String authCode;
  }
}
