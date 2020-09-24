package com.soccerbee.api.system;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemDto {
  private Integer idfNoticeSystem;
  private Integer idfAdmin;
  private String title;
  private String content;
  @DateTimeFormat(pattern = "yyyyMMdd") @JsonFormat(pattern = "yyyyMMdd") private Date dueDate;
}
