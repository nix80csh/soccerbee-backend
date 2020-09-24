package com.soccerbee.api.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soccerbee.common.EnumResponse;


@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired AuthService authService;

  @GetMapping("/verifyJwt")
  public EnumResponse verifyJwt(@RequestHeader("Authorization") String jwt) {
    return authService.verifyJwt(jwt);
  }
}

