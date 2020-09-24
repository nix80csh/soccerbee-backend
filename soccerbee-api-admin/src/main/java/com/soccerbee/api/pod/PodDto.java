package com.soccerbee.api.pod;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PodDto {
  private String[] idfPods;
  private String idfPod;
  private String type;
  private Timestamp regDate;
}
