package com.zhkeen.flyrise.fe.translate.utils;

import java.util.Date;
import java.util.Random;

public class LanguageIdUtil {

  public static long generateId() {
    return new Date().getTime() * 1000 + new Random().nextInt(1000);
  }
}
