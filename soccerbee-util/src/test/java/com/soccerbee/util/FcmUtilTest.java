package com.soccerbee.util;


import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;


@RunWith(MockitoJUnitRunner.class)
public class FcmUtilTest {
  @Spy @InjectMocks FcmUtil fcmUtil;

  @Test
  public void sendAsyncTest() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("title", "혹시 이메시지를 보신다면");
    map.put("message", "저에게 행복한 소식을 전해주세요");

    AndroidConfig androidConfig = AndroidConfig.builder().setTtl(Duration.ofMinutes(2).toMillis())
        .setCollapseKey("chuck").setPriority(Priority.HIGH)
        .setNotification(AndroidNotification.builder().setTag("test").build()).build();

    ApnsConfig apnsConfig = ApnsConfig.builder()
        .setAps(Aps.builder().setCategory("test").setThreadId("chuck").build()).build();

    MulticastMessage multiCast = MulticastMessage.builder().putAllData(map)
        .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
        .setNotification(
            Notification.builder().setTitle("비동기 멀티 테스트").setBody("이 문자가 도착했다면 알려주세요").build())
        .addAllTokens(Arrays.asList(
            "dY-PeyM50Ds:APA91bH4eo4wlEn7iB26UzOkqSzXDuU9DhZTmjauGgXSptRbpIGZQ1P9bpwzV-2P3adC0GYz8qaQRDnkc4tW99ERjuh5v9w6X2Y7SnJbEa7Jq3Oum9UrgjBgWsSwre51MMknq8yNc4h2",
            "fCwERgc5_C4:APA91bHay560RNN6sAi4j35V3-FtB_4WomlZwu4FkTGL2QzP1tPACMhwTM2JKbmERHUo_xG7ohCSzAqQxz0sgfJE9pxStB-fB2AaeX9LP9Vdtqx0MtNl3Ho8h6I_QFHP1Rls3U6Tn_qH"))
        .build();
    fcmUtil.sendAsync(multiCast);
    verify(fcmUtil, atLeastOnce()).sendAsync(multiCast);
  }
}
