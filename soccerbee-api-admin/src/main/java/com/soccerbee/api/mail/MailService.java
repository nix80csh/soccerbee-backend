package com.soccerbee.api.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.model.TemplateMetadata;
import com.soccerbee.api.mail.MailDto.SendTemplatedEmailDto;
import com.soccerbee.api.mail.MailDto.TemplateDto;
import com.soccerbee.common.EnumResponse;
import com.soccerbee.util.AwsSESUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class MailService {
  @Value("${aws.ses.sender}") private String sesSender;
  @Autowired AwsSESUtil awsSESUtil;

  public EnumResponse registTemplate(TemplateDto templateDto) {
    awsSESUtil.registTemplate(templateDto.getTemplateName(), templateDto.getSubject(),
        templateDto.getHtml(), templateDto.getText());
    return EnumResponse.Registered;
  }

  public EnumResponse modifyTemplate(TemplateDto templateDto) {
    awsSESUtil.modifyTemplate(templateDto.getTemplateName(), templateDto.getSubject(),
        templateDto.getHtml(), templateDto.getText());
    return EnumResponse.Modified;
  }

  public EnumResponse deleteTemplate(String templateName) {
    awsSESUtil.deleteTemplate(templateName);
    return EnumResponse.Deleted;
  }

  public EnumResponse sendTemplatedMail(SendTemplatedEmailDto sendTemplatedEmailDto) {
    awsSESUtil.sendTemplatedEmail(sesSender, Arrays.asList(sendTemplatedEmailDto.getToEmail()),
        sendTemplatedEmailDto.getTemplateName(), sendTemplatedEmailDto.getTemplateData());
    return EnumResponse.Sent;
  }

  public List<TemplateDto> getTemplateList() {
    List<TemplateDto> templateDtoList = new ArrayList<TemplateDto>();
    List<TemplateMetadata> templateMetadataList =
        awsSESUtil.getTemplateList().getTemplatesMetadata();
    for (int i = 0; i < templateMetadataList.size(); i++) {
      TemplateDto templateDto = new TemplateDto();
      templateDto.setTemplateName(templateMetadataList.get(i).getName());
      templateDto.setCreatedDate(templateMetadataList.get(i).getCreatedTimestamp());
      templateDtoList.add(templateDto);
    }
    return templateDtoList;
  }
}
