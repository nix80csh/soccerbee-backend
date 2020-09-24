package com.soccerbee.api.mail;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;


public class MailDto {
  @Getter
  @Setter
  public static class TemplateDto {
    private String templateName;
    private String subject;
    private String html;
    private String text;
    private Date createdDate;
  }

  @Getter
  @Setter
  public static class SendTemplatedEmailDto {
    private String templateName;
    private String templateData;
    private String[] toEmail;
  }
}
