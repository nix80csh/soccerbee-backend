package com.soccerbee.security;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.soccerbee.repo.AdminRepo;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired UserDetailService userDetailService;
  @Autowired AdminRepo adminRepo;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
    http.authorizeRequests().antMatchers("/auth/**").permitAll();
    http.authorizeRequests().antMatchers("/util/**").permitAll();
    http.authorizeRequests().antMatchers("/test/auth").authenticated();
    http.authorizeRequests().antMatchers("/test/**").permitAll();
    http.authorizeRequests().anyRequest().authenticated();
    http.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
    http.addFilter(new JWTAuthenticationFilter(authenticationManager(), adminRepo));
    http.addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailService));
  }

  @Override
  protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
    authManagerBuilder.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public AuthenticationEntryPoint unauthorizedEntryPoint() {
    return (request, response, authException) -> response.sendError(
        HttpServletResponse.SC_UNAUTHORIZED,
        "Unauthorized: Authentication token was either missing or invalid");
  }
}
