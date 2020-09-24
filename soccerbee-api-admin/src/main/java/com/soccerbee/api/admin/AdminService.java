package com.soccerbee.api.admin;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.soccerbee.common.EnumResponse;
import com.soccerbee.entity.Admin;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AdminRepo;
import com.soccerbee.util.MailSenderUtil;
import com.soccerbee.util.RandomCharUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AdminService {

  @Value("${cloudinary.root-path}") private String rootPath;

  @Autowired AdminRepo adminRepo;
  @Autowired PasswordEncoder passwordEncoder;

  public EnumResponse regist(AdminDto adminDto) {
    // 존재하는 회원체크
    if (adminRepo.existsByEmail(adminDto.getEmail()))
      throw new LogicException(LogicErrorList.DuplicateEntity_Admin);

    Admin admin = new Admin();
    BeanUtils.copyProperties(adminDto, admin);
    String password = RandomCharUtil.getRandomString(15);
    admin.setPassword(passwordEncoder.encode(password));
    adminRepo.save(admin);

    MailSenderUtil.send(adminDto.getEmail(), "SoccerBee Admin Password Notify", password);
    return EnumResponse.Registered;
  }

  public EnumResponse modify(AdminDto adminDto) {
    Admin admin = adminRepo.findById(adminDto.getIdfAdmin())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Admin));

    admin.setName(adminDto.getName());
    admin.setMobile(adminDto.getMobile());
    admin.setAuthority(adminDto.getAuthority());
    adminRepo.save(admin);
    return EnumResponse.Modified;
  }

  public EnumResponse delete(int idfAdmin) {
    Admin admin = adminRepo.findById(idfAdmin)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Admin));
    adminRepo.delete(admin);
    return EnumResponse.Deleted;
  }

  public List<AdminDto> getList() {
    List<AdminDto> adminDtoList = new ArrayList<AdminDto>();
    for (Admin admin : adminRepo.findAll()) {
      AdminDto adminDto = new AdminDto();
      BeanUtils.copyProperties(admin, adminDto);
      adminDtoList.add(adminDto);
    }
    return adminDtoList;
  }

  public EnumResponse sendPassword(int idfAdmin) {
    Admin admin = adminRepo.findById(idfAdmin)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Admin));

    String password = RandomCharUtil.getRandomString(15);
    admin.setPassword(passwordEncoder.encode(password));
    adminRepo.save(admin);
    MailSenderUtil.send(admin.getEmail(), "SoccerBee Admin Password Notify", password);

    return EnumResponse.Sent;
  }
}
