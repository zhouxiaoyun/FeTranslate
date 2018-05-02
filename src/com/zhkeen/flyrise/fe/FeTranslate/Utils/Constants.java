package com.zhkeen.flyrise.fe.FeTranslate.Utils;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class Constants {

  public static final int MARGIN = 8;
  public static final int TEXTAREA_ROWS = 3;
  public static final int TEXTAREA_COLS = 60;

  public static final String JDBC_PORPERTIES_FILE = "src/main/resources/jdbc.properties";
  public static final String LANGUAGE_PROPERTIES_FILE = "src/main/resources/language.properties";

  public static Map<String, String> ALL_LANGUAGE_MAP = ImmutableMap
      .of("zh_CN", "简体中文", "zh_TW", "繁体中文", "en", "英文");

}
