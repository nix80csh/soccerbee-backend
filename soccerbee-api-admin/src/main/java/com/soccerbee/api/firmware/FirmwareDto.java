package com.soccerbee.api.firmware;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirmwareDto {
  private String version;
  private String type;
  private MultipartFile file;
}
