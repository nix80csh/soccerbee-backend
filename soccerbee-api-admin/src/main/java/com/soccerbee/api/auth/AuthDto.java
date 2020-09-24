package com.soccerbee.api.auth;

import lombok.Data;

@Data
public class AuthDto {
  private String email;
  private String password;
}
