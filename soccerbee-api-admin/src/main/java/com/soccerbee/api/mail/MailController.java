package com.soccerbee.api.mail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soccerbee.api.mail.MailDto.SendTemplatedEmailDto;
import com.soccerbee.api.mail.MailDto.TemplateDto;
import com.soccerbee.common.EnumResponse;

@RestController
@RequestMapping("/mail")
public class MailController {
  @Autowired MailService mailService;

  @PostMapping("/registTemplate")
  public EnumResponse registTemplate(@RequestBody TemplateDto templateDto) {
    return mailService.registTemplate(templateDto);
  }

  @PostMapping("/modifyTemplate")
  public EnumResponse modifyTemplate(@RequestBody TemplateDto templateDto) {
    return mailService.modifyTemplate(templateDto);
  }

  @GetMapping("/getTemplateList")
  public List<TemplateDto> getTemplateList() {
    return mailService.getTemplateList();
  }

  @DeleteMapping("/deleteTemplate/{templateName}")
  public EnumResponse deleteTemplate(@PathVariable String templateName) {
    return mailService.deleteTemplate(templateName);
  }

  @PostMapping("/sendTemplatedMail")
  public EnumResponse sendTemplatedMail(@RequestBody SendTemplatedEmailDto sendTemplatedEmailDto) {
    return mailService.sendTemplatedMail(sendTemplatedEmailDto);
  }
}

