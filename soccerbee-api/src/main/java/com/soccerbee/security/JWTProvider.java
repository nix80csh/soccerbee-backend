package com.soccerbee.security;

import java.util.Date;

import org.springframework.security.authentication.BadCredentialsException;

import com.soccerbee.entity.Account;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTProvider {
  static final String SECRET = "Soccerbee-API";
  static final String TOKEN_PREFIX = "Bearer ";
  static final String HEADER_STRING = "Authorization";
  static final long EXPIRATION_TIME = 86400000L * 7;

  public static String getEmail(String jwt) {
    try {
      String userId = (String) Jwts.parser().setSigningKey(SECRET)
          .parseClaimsJws(jwt.replace(TOKEN_PREFIX, "")).getBody().get("email");
      return userId;
    } catch (RuntimeException e) {
      throw new BadCredentialsException("");
    }
  }

  public static String createJwt(Account account) {
    return Jwts.builder().claim("idfAccount", account.getIdfAccount())
        .claim("email", account.getEmail()).claim("authCode", account.getAuthCode())
        .claim("name", account.getName())
        .claim("position", account.getAccountPlayer().getPosition())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(SignatureAlgorithm.HS512, SECRET).compact();
  }
}
