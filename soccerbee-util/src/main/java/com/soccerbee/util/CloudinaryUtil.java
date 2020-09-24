package com.soccerbee.util;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CloudinaryUtil {

  private static final String API_KEY = "147792778212748";
  private static final String API_SECRET = "AbZwS4rynw5VkQtjiCvHx9z9vJU";
  private static final String NAME = "ubeeslab";

  private static Cloudinary cloudinary() {
    return new Cloudinary(
        ObjectUtils.asMap("cloud_name", NAME, "api_key", API_KEY, "api_secret", API_SECRET));
  }

  public static String upload(String dirPath, MultipartFile image) {
    String url = null;
    try {
      // TODO 공통코드 private 메소드 추출
      Map params = ObjectUtils.asMap("folder", dirPath);
      Map resultMap = cloudinary().uploader().upload(image.getBytes(), params);

      Integer version = (Integer) resultMap.get("version");
      String publicId = (String) resultMap.get("public_id");
      String format = (String) resultMap.get("format");
      url = "/v" + version.toString() + "/" + publicId + "." + format;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return url;
  }

  public static String upload(String dirPath, Integer idf, MultipartFile image) {
    String url = null;
    try {
      Map params = ObjectUtils.asMap("public_id", idf.toString(), "folder", dirPath);
      Map resultMap = cloudinary().uploader().upload(image.getBytes(), params);
      Integer version = (Integer) resultMap.get("version");
      String publicId = (String) resultMap.get("public_id");
      String format = (String) resultMap.get("format");
      url = "/v" + version.toString() + "/" + publicId + "." + format;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return url;
  }

  public static void delete(String dirPath) {
    try {
      String[] split = dirPath.split("/");
      String file = split[4];
      String dir = split[3];
      String env = split[2];
      int dotIndex = file.lastIndexOf(".");
      String fileName = file.substring(0, dotIndex);
      Map result = cloudinary().uploader().destroy(env + "/" + dir + "/" + fileName,
          ObjectUtils.asMap("invalidate", true));

      log.info("삭제 완료시 ok =>> " + result.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
