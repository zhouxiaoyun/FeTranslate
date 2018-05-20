package com.zhkeen.flyrise.fe.translate.utils;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Constants {

  public static final int MARGIN = 8;
  public static final int TEXTAREA_ROWS = 3;
  public static final int TEXTAREA_COLS = 60;

  public static final String JDBC_PORPERTIES_FILE = "properties/jdbc.properties";

  public static final Pattern PATTERN_TRANSLATE = Pattern.compile("[0-9]{16}");
  public static Map<String, String> ALL_LANGUAGE_MAP = getAllLanguageMap();

  public static final String JAVA_FORMART = "translateUtil.get(%sL)";
  public static final String JSP_FORMART = "<%%=translateUtil.get(%sL)%%>";
  public static final String JS_FORMART = "translateUtil.s%s";
  public static final String HTML_FORMART = "translateUtil.s%s";

  private static Map<String, String> getAllLanguageMap(){
    Map<String, String> allMap = new LinkedHashMap<>();
    allMap.put("ZH","简体中文");
    allMap.put("CHT","繁体中文");
    allMap.put("EN","英文");
    allMap.put("JP","日语");
    allMap.put("KOR","韩语");
    allMap.put("FRA","法语");
    allMap.put("SPA","西班牙语");
    allMap.put("RU","俄语");
    allMap.put("DE","德语");
    allMap.put("PT","葡萄牙语");
    return allMap;
  }

}
