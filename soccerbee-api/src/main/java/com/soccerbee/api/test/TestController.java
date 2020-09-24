package com.soccerbee.api.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
  @Value("${service-domain}") private String serviceDomain;

  @GetMapping("/auth")
  public boolean testMsg() {
    return true;
  }

  @GetMapping("/ping")
  public boolean testPing() {
    return false;
  }

  @GetMapping("/healthcheck")
  public boolean healthcheck() {
    return true;
  }
}
