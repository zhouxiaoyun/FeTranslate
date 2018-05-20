package com.zhkeen.flyrise.fe.translate.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class LanguageIdUtil {

  public static String generateId() {
    String id = String.valueOf(new Date().getTime() * 1000 + new Random().nextInt(1000));
    id.substring(4);
    return id;
  }
}
