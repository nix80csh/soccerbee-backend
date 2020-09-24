package com.soccerbee.util;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FcmUtil {
  private FcmUtil() throws IOException {
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    InputStream fcmJson = classloader.getResourceAsStream("fcm.json");
    FirebaseOptions options =
        new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(fcmJson))
            .setDatabaseUrl("https://smarteleven-a978c.firebaseio.com").build();
    if (FirebaseApp.getApps().isEmpty())
      FirebaseApp.initializeApp(options);
  }

  public void sendAsync(MulticastMessage multiCast) {
    FirebaseMessaging.getInstance().sendMulticastAsync(multiCast);
  }
}
