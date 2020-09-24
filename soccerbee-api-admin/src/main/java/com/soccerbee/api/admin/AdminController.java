package com.soccerbee.api.admin;

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
@RequestMapping("/admin")
public class AdminController {

  @Autowired AdminService adminService;

  @PostMapping("/regist")
  public EnumResponse regist(@RequestBody AdminDto adminDto) {
    return adminService.regist(adminDto);
  }

  @PostMapping("/modify")
  public EnumResponse modify(@RequestBody AdminDto adminDto) {
    return adminService.modify(adminDto);
  }

  @DeleteMapping("/delete/{idfAdmin}")
  public EnumResponse delete(@PathVariable int idfAdmin) {
    return adminService.delete(idfAdmin);
  }

  @GetMapping("/getList")
  public List<AdminDto> getList() {
    return adminService.getList();
  }

  @GetMapping("/sendPassword/{idfAdmin}")
  public EnumResponse sendPassword(@PathVariable int idfAdmin) {
    return adminService.sendPassword(idfAdmin);
  }

}

