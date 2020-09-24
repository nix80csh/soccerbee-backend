package com.soccerbee.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AmazonS3Util {
  private static final String accessKey = "AKIA3D45T4KQWIVGRGHG";
  private static final String secretKey = "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";
  private static final String region = "ap-northeast-2";

  private static AmazonS3 s3client() {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
    return s3Client;
  }

  public static void uploadFile(String bucketName, String fileName, MultipartFile mFile,
      String contentType) {
    try {
      ObjectMetadata objMeta = new ObjectMetadata();
      objMeta.setContentLength(mFile.getBytes().length);
      objMeta.setContentType(contentType);
      PutObjectRequest putObjectRequest =
          new PutObjectRequest(bucketName, fileName, mFile.getInputStream(), objMeta);
      putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
      s3client().putObject(putObjectRequest);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void uploadFile(String bucketName, String fileName, File file, String contentType) {
    ObjectMetadata objMeta = new ObjectMetadata();
    objMeta.setContentType(contentType);
    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
    putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
    putObjectRequest.setMetadata(objMeta);
    s3client().putObject(putObjectRequest);
  }

  public static S3Object getS3Object(String bucketName, String fileName) {
    GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName);
    return s3client().getObject(getObjectRequest);
  }

  public static void copyFile(String oldBucketName, String oldFileName, String newBucketName,
      String newFileName) {
    ObjectMetadata objMeta = s3client().getObjectMetadata(oldBucketName, oldFileName);
    CopyObjectRequest copyObjectRequest =
        new CopyObjectRequest(oldBucketName, oldFileName, newBucketName, newFileName)
            .withNewObjectMetadata(objMeta)
            .withCannedAccessControlList(CannedAccessControlList.PublicRead);
    s3client().copyObject(copyObjectRequest);
  }

  public static void deleteFile(String bucketName, String fileName) {
    s3client().deleteObject(bucketName, fileName);
  }

  private static void displayTextInputStream(InputStream input) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));
      String line = null;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
      System.out.println();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<S3ObjectSummary> getS3ObjectList(String bucketName, String prefix) {
    List<S3ObjectSummary> s3ObjectSummaryList =
        s3client().listObjectsV2(bucketName, prefix).getObjectSummaries();
    return s3ObjectSummaryList;
  }
}
