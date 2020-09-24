package com.soccerbee.batch;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GPSVo {
  private Long t;
  private Double x;
  private Double y;
}
