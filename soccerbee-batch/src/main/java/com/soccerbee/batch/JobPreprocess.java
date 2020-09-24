package com.soccerbee.batch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soccerbee.entity.Pod;
import com.soccerbee.repo.PodRepo;
import com.soccerbee.util.AmazonS3Util;
import com.soccerbee.util.SlackNotificationUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JobPreprocess {
  @Value("${spring.profiles.active}") private String activeProfile;
  @Autowired JobBuilderFactory jobBuilderFactory;
  @Autowired StepBuilderFactory stepBuilderFactory;
  @Autowired PodRepo podRepo;
  private int chunkSize = 1000;

  @Bean
  public Job preprocessJob() {
    return jobBuilderFactory.get("preprocessJob").start(preprocessStep(null)).build();
  }

  @Bean
  @JobScope
  public Step preprocessStep(@Value("#{jobParameters[ubsPath]}") String ubsPath) {
    log.info(">>> preprocessJob >> preprocessStep > 1 > ubsPath: " + ubsPath);
    return stepBuilderFactory.get("preprocessStep").tasklet((contribution, chunkContext) -> {
      String[] arrayUbsPath = ubsPath.split("\\_");
      Pod pod = podRepo.findById(arrayUbsPath[1]).orElse(null);
      if (pod != null) {
        S3Object s3Object = AmazonS3Util.getS3Object("soccerbee-" + activeProfile, ubsPath);
        byte[] bytes = IOUtils.toByteArray(s3Object.getObjectContent());
        String json = convert(bytes);

        File file = new File(RandomStringUtils.randomAlphanumeric(10) + ".json");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(json);
        fileWriter.close();
        AmazonS3Util.uploadFile("soccerbee-" + activeProfile, ubsPath.replace(".ubs", ".json"),
            file, "application/json");
        file.delete();
        // AmazonS3Util.deleteFile("soccerbee-" + activeProfile, ubsPath);
        String message = "soccerbee-batch-" + activeProfile + "\n ubsPath: " + ubsPath + "\n 이메일: "
            + pod.getAccount().getEmail() + "\n 이름: " + pod.getAccount().getName() + "\n json url: "
            + "https://soccerbee-" + activeProfile + ".s3.ap-northeast-2.amazonaws.com/"
            + ubsPath.replace(".ubs", ".json");
        SlackNotificationUtil.send("notify", "SoccerBee Bot", message);
      }
      return RepeatStatus.FINISHED;
    }).build();
  }

  private String convert(byte[] bytes) throws IOException {
    int totalBytePerLine = 14;
    byte[] byteLine =
        {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    int totalRow = bytes.length / totalBytePerLine;
    List<GPSVo> gpsVoList = new ArrayList<GPSVo>();
    for (int i = 0; i < totalRow; i++) {
      for (int j = 0; j < totalBytePerLine; j++) {
        byteLine[j] = bytes[i * totalBytePerLine + j];
      }
      byte[] byteDateTime = {byteLine[3], byteLine[2], byteLine[1], byteLine[0]};
      byte[] byteMillisecond = {byteLine[5], byteLine[4]};
      byte[] byteLatitude = {byteLine[9], byteLine[8], byteLine[7], byteLine[6]};
      byte[] byteLongitude = {byteLine[13], byteLine[12], byteLine[11], byteLine[10]};

      long unixTime = Long.parseLong(
          byteArrayToInt(byteDateTime) + String.format("%03d", byteArrayToInt(byteMillisecond)));
      double latitude = Double.parseDouble(
          String.format(Locale.US, "%4.9f", convertGPSaxis(byteArrayToInt(byteLatitude))));
      double longitude = Double.parseDouble(
          String.format(Locale.US, "%4.9f", convertGPSaxis(byteArrayToInt(byteLongitude))));
      gpsVoList.add(new GPSVo.GPSVoBuilder().t(unixTime).x(latitude).y(longitude).build());
    }
    return new ObjectMapper().writeValueAsString(gpsVoList);
  }

  private double convertGPSaxis(int preGPSaxis) {
    int axis_1st = 0; // 1차 값
    double axis_2_1 = 0; // 2-1차 값
    double axis_2_2 = 0; // 2-2차 값
    double postGPSaxis = 0;
    // Step 1. 1차 값
    axis_1st = (int) (preGPSaxis / 1000000);
    // Step 2. 2차 값
    // axis_2_1 = (double) (preGPSaxis / 10000);
    axis_2_1 = (double) preGPSaxis / 10000;
    axis_2_2 = (double) ((axis_2_1 % 100) / 60);
    postGPSaxis = axis_1st + axis_2_2;
    return postGPSaxis;
  }

  private int byteArrayToInt(byte[] bytes) {
    final int size = Integer.SIZE / 8;
    final byte[] newBytes = new byte[size];
    for (int i = 0; i < size; i++) {
      if (i + bytes.length < size)
        newBytes[i] = (byte) 0x00;
      else
        newBytes[i] = bytes[i + bytes.length - size];
    }
    return ByteBuffer.wrap(newBytes).order(ByteOrder.BIG_ENDIAN).getInt();
  }
}
