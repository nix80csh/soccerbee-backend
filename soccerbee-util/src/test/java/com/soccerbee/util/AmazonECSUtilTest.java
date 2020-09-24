package com.soccerbee.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;

import com.amazonaws.services.ecs.model.AwsVpcConfiguration;
import com.amazonaws.services.ecs.model.KeyValuePair;

public class AmazonECSUtilTest {

  @Test
  public void testRunTask() {
    Collection<KeyValuePair> env = new ArrayList<KeyValuePair>();
    AwsVpcConfiguration vpcConfig = new AwsVpcConfiguration();
    vpcConfig.setSecurityGroups(Arrays.asList("sg-07fba86dd4504ccd8"));
    vpcConfig.setSubnets(Arrays.asList("subnet-c4f10daf", "subnet-6229092e", "subnet-3eb41d45"));
    vpcConfig.setAssignPublicIp("ENABLED");
    AmazonECSUtil.runTask("soccerbee", "batch-dev", 1,
        Arrays.asList("java", "-jar", "/usr/app/soccerbee-batch.jar",
            "--spring.batch.job.names=preprocessJob", "JobName=preprocessJob",
            "ubsPath=gpsdata/119_DC18F45389F0_1587692440_1587695524_1587738761.ubs",
            "timestamp=" + System.currentTimeMillis(), "Date=" + new Date()),
        env, vpcConfig);
  }
}
