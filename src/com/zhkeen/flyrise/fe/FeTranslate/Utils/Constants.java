package com.zhkeen.flyrise.fe.FeTranslate.Utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

  public static final int MARGIN = 10;
  public static final int LABEL_WIDTH = 60;
  public static final int LABEL_ID_WIDTH = 100;
  public static final int LABEL_HEIGHT = 20;
  public static final int TEXTAREA_WIDTH = 300;
  public static final int TEXTAREA_HEIGHT = 50;
  public static final int TEXTAREA_ROWS = 10;
  public static final int TEXTAREA_COLS = 20;
  public static final int BUTTON_WIDTH = 50;
  public static final int BUTTON_HIGHT = 25;

  public static final String JDBC_PORPERTIES_FILE = "src/main/resources/jdbc.properties";
  public static final String LANGUAGE_PROPERTIES_FILE = "src/main/resources/language.properties";

  public static final Map<String, String> ALL_LANGUAGE_MAP = new HashMap<String, String>() {{
    ALL_LANGUAGE_MAP.put("zh_CN", "简体中文");
    ALL_LANGUAGE_MAP.put("zh_TW", "繁体中文");
    ALL_LANGUAGE_MAP.put("en", "英文");
  }};

}