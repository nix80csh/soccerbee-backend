package com.soccerbee.api.news;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.soccerbee.common.EnumResponse;
import com.soccerbee.entity.Admin;
import com.soccerbee.entity.NoticeNew;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AdminRepo;
import com.soccerbee.repo.NoticeNewRepo;
import com.soccerbee.util.CloudinaryUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class NewsService {
  @Value("${cloudinary.root-path}") private String rootPath;
  @Autowired AdminRepo adminRepo;
  @Autowired NoticeNewRepo noticeNewRepo;

  public EnumResponse regist(NewsDto newsDto) {
    Admin admin = adminRepo.findById(newsDto.getIdfAdmin())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Admin));

    String imageUrl = CloudinaryUtil.upload(rootPath + "/news", newsDto.getMFile());

    NoticeNew noticeNew = new NoticeNew();
    noticeNew.setTitle(newsDto.getTitle());
    noticeNew.setContent(newsDto.getContent());
    noticeNew.setDueDate(newsDto.getDueDate());
    noticeNew.setImage(imageUrl);
    noticeNew.setAdmin(admin);
    noticeNewRepo.save(noticeNew);
    return EnumResponse.Registered;
  }

  public EnumResponse modify(NewsDto newsDto) {
    Admin admin = adminRepo.findById(newsDto.getIdfAdmin())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Admin));

    NoticeNew noticeNew = noticeNewRepo.findById(newsDto.getIdfNoticeNew())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_NoticeNew));

    noticeNew.setIdfNoticeNew(newsDto.getIdfNoticeNew());
    noticeNew.setAdmin(admin);
    noticeNew.setTitle(newsDto.getTitle());
    noticeNew.setContent(newsDto.getContent());
    noticeNew.setDueDate(newsDto.getDueDate());
    noticeNewRepo.save(noticeNew);
    return EnumResponse.Modified;
  }

  public EnumResponse modifyImage(NewsDto newsDto) {
    Admin admin = adminRepo.findById(newsDto.getIdfAdmin())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Admin));

    NoticeNew noticeNew = noticeNewRepo.findById(newsDto.getIdfNoticeNew())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_NoticeNew));
    CloudinaryUtil.delete(noticeNew.getImage());
    String imageUrl = CloudinaryUtil.upload(rootPath + "/news", newsDto.getMFile());
    noticeNew.setAdmin(admin);
    noticeNew.setImage(imageUrl);
    return EnumResponse.Modified;
  }

  public List<NewsDto> getList() {
    List<NewsDto> newsDtoList = new ArrayList<NewsDto>();
    for (NoticeNew noticeNew : noticeNewRepo.findAll()) {
      NewsDto newsDto = new NewsDto();
      BeanUtils.copyProperties(noticeNew, newsDto);
      newsDto.setIdfAdmin(noticeNew.getAdmin().getIdfAdmin());
      newsDtoList.add(newsDto);
    }
    return newsDtoList;
  }

  public EnumResponse delete(Integer idfNoticeNew) {
    NoticeNew noticeNew = noticeNewRepo.findById(idfNoticeNew)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_NoticeNew));

    CloudinaryUtil.delete(noticeNew.getImage());
    noticeNewRepo.delete(noticeNew);
    return EnumResponse.Deleted;
  }
}
