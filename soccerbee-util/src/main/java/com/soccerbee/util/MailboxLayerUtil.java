package com.soccerbee.util;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public class MailboxLayerUtil {

  private static final String mailboxLayerKey = "e0ffbf860ccdfe1a8c3edf91a69b37ca";

  public static boolean checkValid(String email) {

    String baseUrl = "http://apilayer.net/api/check?access_key=" + mailboxLayerKey
        + "&email={email}&smtp=1&format=1";
    RestTemplate restTemplate = new RestTemplate();
    MailboxLayerVO mailboxLayerVO = restTemplate.getForObject(baseUrl, MailboxLayerVO.class, email);

    if (mailboxLayerVO.getFormatValid() == true && mailboxLayerVO.getMxFound() == true
        && mailboxLayerVO.getSmtpCheck() == true) {
      return true;
    } else {
      return false;
    }
  }

  @Data
  public static class MailboxLayerVO {
    @JsonProperty("format_valid") private Boolean formatValid;
    @JsonProperty("mx_found") private Boolean mxFound;
    @JsonProperty("smtp_check") private Boolean smtpCheck;
  }

}
