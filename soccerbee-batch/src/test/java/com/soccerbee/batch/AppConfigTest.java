package com.soccerbee.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AppConfig.class, BatchTestConfig.class})
public class AppConfigTest {

  @Test
  public void contextLoads() {}

}
