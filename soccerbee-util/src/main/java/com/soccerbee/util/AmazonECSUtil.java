package com.soccerbee.util;

import java.util.Collection;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.AmazonECSClient;
import com.amazonaws.services.ecs.model.AwsVpcConfiguration;
import com.amazonaws.services.ecs.model.ContainerOverride;
import com.amazonaws.services.ecs.model.KeyValuePair;
import com.amazonaws.services.ecs.model.NetworkConfiguration;
import com.amazonaws.services.ecs.model.RunTaskRequest;
import com.amazonaws.services.ecs.model.TaskOverride;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AmazonECSUtil {
  private static final String accessKey = "AKIA3D45T4KQZCLFK224";
  private static final String secretKey = "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";
  private static final String region = "ap-northeast-2";

  private static AmazonECS ecsClient() {
    BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
    AWSStaticCredentialsProvider credentialsProvider =
        new AWSStaticCredentialsProvider(credentials);
    AmazonECS ecsClient =
        AmazonECSClient.builder().withCredentials(credentialsProvider).withRegion(region).build();
    return ecsClient;
  }

  public static void runTask(String clusterName, String taskName, int count,
      Collection<String> command, Collection<KeyValuePair> env, AwsVpcConfiguration vpcConfig) {
    ContainerOverride containerOverride = new ContainerOverride();
    containerOverride.setName(taskName);
    containerOverride.setCommand(command);
    containerOverride.setEnvironment(env);
    // containerOverride.setMemoryReservation(8000);
    RunTaskRequest runTaskRequest = new RunTaskRequest();
    runTaskRequest.setCluster(clusterName);
    runTaskRequest.setGroup(taskName);
    runTaskRequest.setTaskDefinition(taskName);
    runTaskRequest.setLaunchType("FARGATE");
    runTaskRequest.setCount(1);
    runTaskRequest
        .setNetworkConfiguration(new NetworkConfiguration().withAwsvpcConfiguration(vpcConfig));
    runTaskRequest.setOverrides(new TaskOverride().withContainerOverrides(containerOverride));
    ecsClient().runTask(runTaskRequest);
  }
}
