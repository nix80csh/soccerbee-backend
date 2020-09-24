package com.soccerbee.api.pod;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soccerbee.common.EnumResponse;

@RestController
@RequestMapping("/pod")
public class PodController {
  @Autowired PodService podService;

  @PostMapping("/regist")
  public EnumResponse regist(@RequestBody PodDto podDto) {
    return podService.regist(podDto);
  }

  @GetMapping("/getList")
  public List<PodDto> getList() {
    return podService.getList();
  }

  @DeleteMapping("/delete/{idfPod}")
  public EnumResponse delete(@PathVariable String idfPod) {
    return podService.delete(idfPod);
  }
}

