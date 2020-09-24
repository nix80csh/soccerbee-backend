package com.soccerbee.api.system;

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
@RequestMapping("/system")
public class SystemController {
  @Autowired SystemService systemService;

  @PostMapping("/regist")
  public EnumResponse regist(@RequestBody SystemDto systemDto) {
    return systemService.regist(systemDto);
  }

  @PostMapping("/modify")
  public EnumResponse modify(@RequestBody SystemDto systemDto) {
    return systemService.modify(systemDto);
  }

  @GetMapping("/getList")
  public List<SystemDto> getList() {
    return systemService.getList();
  }

  @DeleteMapping("/delete/{idfNoticeSystem}")
  public EnumResponse delete(@PathVariable Integer idfNoticeSystem) {
    return systemService.delete(idfNoticeSystem);
  }
}

