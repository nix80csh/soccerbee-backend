package com.soccerbee.security;


import static com.soccerbee.security.JWTProvider.HEADER_STRING;
import static com.soccerbee.security.JWTProvider.TOKEN_PREFIX;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.soccerbee.api.auth.AuthDto;
import com.soccerbee.entity.Admin;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AdminRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;
  private AdminRepo adminRepo;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AdminRepo adminRepo) {
    super.setFilterProcessesUrl("/auth/signin");
    this.authenticationManager = authenticationManager;
    this.adminRepo = adminRepo;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      log.info("attempt auth 진입");
      AuthDto authDto = new ObjectMapper().readValue(request.getInputStream(), AuthDto.class);

      return this.authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));

    } catch (IOException | AuthenticationException e) {
      log.error("attemptAuthentication >> signin error");
      throw new BadCredentialsException("");
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    String email = ((User) authResult.getPrincipal()).getUsername();
    Admin admin = adminRepo.findByEmail(email)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Admin));
    log.info("successful auth 진입 email : " + email);
    String token = JWTProvider.createJwt(admin);
    String bearerToken = TOKEN_PREFIX + token;

    Map<String, String> map = new HashMap<String, String>();
    map.put(HEADER_STRING, bearerToken);
    String json = new Gson().toJson(map);
    response.setContentType("application/json");
    response.getWriter().write(json);
    // response.addHeader(HEADER_STRING, bearerToken);
  }
}

