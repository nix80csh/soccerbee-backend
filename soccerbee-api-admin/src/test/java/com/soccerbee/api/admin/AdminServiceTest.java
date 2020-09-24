package com.soccerbee.api.admin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.soccerbee.common.EnumResponse;
import com.soccerbee.repo.AdminRepo;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminServiceTest {
  @Autowired AdminRepo adminRepo;
  @Autowired AdminService adminService;

  @Test
  public void registDanielTest() {
    AdminDto adminDto = new AdminDto();
    adminDto.setEmail("daniel.cho@ubeeslab.com");
    adminDto.setMobile("01049209123");
    adminDto.setName("조성훈");
    adminDto.setAuthority("admin");
    EnumResponse enumResponse = adminService.regist(adminDto);
    assertEquals("Registered", enumResponse);
  }

  @Test
  public void registTunaTest() {
    AdminDto adminDto = new AdminDto();
    adminDto.setEmail("tuna3636@ubeeslab.com");
    adminDto.setMobile("01000000000");
    adminDto.setName("박수경");
    adminDto.setAuthority("admin");
    EnumResponse enumResponse = adminService.regist(adminDto);
    assertEquals("Registered", enumResponse);
  }
}
