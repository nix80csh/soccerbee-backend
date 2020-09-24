package com.soccerbee.api.firmware;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soccerbee.common.EnumResponse;

@RestController
@RequestMapping("/firmware")
public class FirmwareController {
  @Autowired FirmwareService firmwareService;

  @PostMapping("/regist")
  public EnumResponse regist(FirmwareDto firmwareDto) {
    return firmwareService.regist(firmwareDto);
  }

  @GetMapping("/getList")
  public List<FirmwareDto> getList() {
    return firmwareService.getList();
  }

  @DeleteMapping("/delete/{type}/{version}")
  public EnumResponse delete(@PathVariable String type, @PathVariable String version) {
    return firmwareService.delete(type, version);
  }
}

