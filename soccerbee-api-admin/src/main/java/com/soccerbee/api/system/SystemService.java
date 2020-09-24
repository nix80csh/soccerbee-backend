package com.soccerbee.api.system;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soccerbee.common.EnumResponse;
import com.soccerbee.entity.Admin;
import com.soccerbee.entity.NoticeSystem;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AdminRepo;
import com.soccerbee.repo.NoticeSystemRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class SystemService {
  @Autowired AdminRepo adminRepo;
  @Autowired NoticeSystemRepo noticeSystemRepo;

  public EnumResponse regist(SystemDto systemDto) {
    Admin admin = adminRepo.findById(systemDto.getIdfAdmin())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Admin));

    NoticeSystem noticeSystem = new NoticeSystem();
    noticeSystem.setTitle(systemDto.getTitle());
    noticeSystem.setContent(systemDto.getContent());
    noticeSystem.setAdmin(admin);
    noticeSystemRepo.save(noticeSystem);
    return EnumResponse.Registered;
  }

  public EnumResponse modify(SystemDto systemDto) {
    Admin admin = adminRepo.findById(systemDto.getIdfAdmin())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Admin));

    NoticeSystem noticeSystem = noticeSystemRepo.findById(systemDto.getIdfNoticeSystem())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_NoticeSystem));

    noticeSystem.setIdfNoticeSystem(systemDto.getIdfNoticeSystem());
    noticeSystem.setAdmin(admin);
    noticeSystem.setTitle(systemDto.getTitle());
    noticeSystem.setContent(systemDto.getContent());
    noticeSystemRepo.save(noticeSystem);
    return EnumResponse.Modified;
  }

  public List<SystemDto> getList() {
    List<SystemDto> systemDtoList = new ArrayList<SystemDto>();
    for (NoticeSystem noticeSystem : noticeSystemRepo.findAll()) {
      SystemDto systemDto = new SystemDto();
      BeanUtils.copyProperties(noticeSystem, systemDto);
      systemDto.setIdfAdmin(noticeSystem.getAdmin().getIdfAdmin());
      systemDtoList.add(systemDto);
    }
    return systemDtoList;
  }

  public EnumResponse delete(Integer idfNoticeSystem) {
    NoticeSystem noticeSystem = noticeSystemRepo.findById(idfNoticeSystem)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_NoticeSystem));

    noticeSystemRepo.delete(noticeSystem);
    return EnumResponse.Deleted;
  }
}
