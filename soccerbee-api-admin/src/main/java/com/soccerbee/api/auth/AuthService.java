package com.soccerbee.api.auth;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.soccerbee.common.EnumResponse;
import com.soccerbee.security.JWTProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AuthService {
  public EnumResponse verifyJwt(String jwt) {
    String email = JWTProvider.getEmail(jwt);
    log.info(email);
    return EnumResponse.Verified;
  }
}
