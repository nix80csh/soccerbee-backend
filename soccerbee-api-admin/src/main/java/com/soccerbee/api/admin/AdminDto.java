package com.soccerbee.api.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDto {
  private int idfAdmin;
  private String email;
  private String name;
  private String mobile;
  private String authority;

  @Getter
  @Setter
  public static class PasswordDto {
    private int idfAdmin;
    private String password;
  }
}
