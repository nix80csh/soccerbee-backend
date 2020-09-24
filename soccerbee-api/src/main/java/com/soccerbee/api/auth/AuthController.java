package com.soccerbee.api.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soccerbee.api.auth.AuthDto.AuthCodeDto;
import com.soccerbee.api.auth.AuthDto.SendPasswordDto;
import com.soccerbee.common.EnumResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired AuthService authService;

  @PostMapping("/signup")
  public EnumResponse signup(@RequestBody AuthDto authDto) {
    return authService.signup(authDto);
  }

  @GetMapping("/verifyEmail/{email}")
  public EnumResponse verifyEmail(@PathVariable String email) {
    return authService.verifyEmail(email);
  }

  @PostMapping("/sendPassword")
  public EnumResponse sendPassword(@RequestBody SendPasswordDto sendPasswordDto) {
    return authService.sendPassword(sendPasswordDto);
  }

  @PostMapping("/authCode")
  public EnumResponse authCode(@RequestBody AuthCodeDto authCodeDto) {
    return authService.authCode(authCodeDto);
  }

  @DeleteMapping("/deleteDeviceToken/{idfAccount}/{idfDevice}")
  public EnumResponse deleteDeviceToken(@PathVariable int idfAccount,
      @PathVariable String idfDevice) {
    return authService.deleteDeviceToken(idfAccount, idfDevice);
  }

  @GetMapping("/verifyJwt")
  public EnumResponse verifyJwt(@RequestHeader("Authorization") String jwt) {
    return authService.verifyJwt(jwt);
  }
}

