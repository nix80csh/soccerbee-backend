package com.soccerbee.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@EnableBatchProcessing
public class BatchTestConfig {

  @Bean
  JobLauncherTestUtils jobLauncherTestUtils() {
    return new JobLauncherTestUtils();
  }

}
