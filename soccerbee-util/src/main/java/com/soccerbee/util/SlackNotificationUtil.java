package com.soccerbee.util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SlackNotificationUtil {

  private static final String token =
      "xoxp-774129990295-760799720499-939698437664-f9de716afc4030286f267c3281eca894";

  public static boolean send(String channelName, String userName, String message) {

    try {
      HttpResponse<String> response = Unirest.post("https://slack.com/api/chat.postMessage")
          .header("Content-Type", "application/json").header("Authorization", "Bearer " + token)
          .header("Cache-Control", "no-cache")
          .header("Postman-Token", "e065d31c-d33b-4e7e-81d6-a3e03937c87b")
          .body("{\n  " + "\"channel\" : \"" + channelName + "\"," + "\n  \"username\" : \""
              + userName + "\"," + "\n  \"text\" : \"" + message + "\"," + "\n  \"as_user\" : false"
              + "\n}")
          .asString();

    } catch (UnirestException e) {
      return false;
    }

    return true;
  }


}
