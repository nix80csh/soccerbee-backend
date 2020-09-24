package com.soccerbee.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClient;
import com.amazonaws.services.simpleemail.model.CreateTemplateRequest;
import com.amazonaws.services.simpleemail.model.DeleteTemplateRequest;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.ListTemplatesRequest;
import com.amazonaws.services.simpleemail.model.ListTemplatesResult;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.amazonaws.services.simpleemail.model.Template;
import com.amazonaws.services.simpleemail.model.UpdateTemplateRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AwsSESUtil {
  @Value("${aws.ses.key}") private String awsSesKey;
  @Value("${aws.ses.secret}") private String awsSesSecret;

  private AmazonSimpleEmailServiceAsync amazonSimpleEmailServiceAsync() {
    BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsSesKey, awsSesSecret);
    return AmazonSimpleEmailServiceAsyncClient.asyncBuilder()
        .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
        .withRegion(Regions.AP_NORTHEAST_2).build();
  }


  public void registTemplate(String templateName, String subject, String html, String text) {
    Template template = new Template();
    template.setTemplateName(templateName);
    template.setSubjectPart(subject);
    template.setHtmlPart(html);
    template.setTextPart(text);
    CreateTemplateRequest request = new CreateTemplateRequest().withTemplate(template);
    amazonSimpleEmailServiceAsync().createTemplateAsync(request);
  }

  public void modifyTemplate(String templateName, String subject, String html, String text) {
    Template template = new Template();
    template.setTemplateName(templateName);
    template.setSubjectPart(subject);
    template.setHtmlPart(html);
    template.setTextPart(text);
    UpdateTemplateRequest request = new UpdateTemplateRequest().withTemplate(template);
    amazonSimpleEmailServiceAsync().updateTemplateAsync(request);
  }

  public ListTemplatesResult getTemplateList() {
    return amazonSimpleEmailServiceAsync().listTemplates(new ListTemplatesRequest());
  }

  public void sendTemplatedEmail(String fromEmail, List<String> toEmailList, String templateName,
      String templateData) {
    SendTemplatedEmailRequest request = new SendTemplatedEmailRequest();
    Destination destination = new Destination();
    destination.setToAddresses(toEmailList);
    request.setTemplate(templateName);
    request.setSource(fromEmail);
    request.setDestination(destination);
    request.setTemplateData(templateData);
    amazonSimpleEmailServiceAsync().sendTemplatedEmailAsync(request);
  }

  public void deleteTemplate(String templateName) {
    DeleteTemplateRequest deleteTemplateRequest = new DeleteTemplateRequest();
    deleteTemplateRequest.setTemplateName(templateName);
    amazonSimpleEmailServiceAsync().deleteTemplateAsync(deleteTemplateRequest);
  }
}
