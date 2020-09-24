package com.soccerbee.batch;

import java.util.Date;

import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public class JobPreprocessTest extends AppConfigTest {

  @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired @Qualifier("preprocessJob") private Job preprocessJob;

  @Test
  public void testPreprocessJob() throws Exception {
    JobParameters jobParameters =
        new JobParametersBuilder().addString("JobName", "testPreprocessJob")
            .addString("ubsPath", "gpsdata/84_FEA1AB9C347E_1587609600_1587611846_1587620870.ubs")
            .addLong("TimeStamp", System.currentTimeMillis()).addDate("Date", new Date())
            .toJobParameters();
    jobLauncherTestUtils.setJob(preprocessJob);
    jobLauncherTestUtils.launchJob(jobParameters);
  }
}
