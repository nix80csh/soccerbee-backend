package com.soccerbee.api.news;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsDto {

  private Integer idfNoticeNew;
  private Integer idfAdmin;
  private String title;
  private String image;
  private MultipartFile mFile;
  private String content;
  @DateTimeFormat(pattern = "yyyyMMdd") @JsonFormat(pattern = "yyyyMMdd") private Date dueDate;
}
