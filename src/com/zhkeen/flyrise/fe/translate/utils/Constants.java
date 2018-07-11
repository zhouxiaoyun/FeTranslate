package com.zhkeen.flyrise.fe.translate.utils;

import com.google.common.collect.ImmutableMap;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Constants {

  public static final int MARGIN = 8;
  public static final int TEXTAREA_ROWS = 3;
  public static final int TEXTAREA_COLS = 60;

  public static final String JDBC_PORPERTIES_FILE = "properties/jdbc.properties";

  public static final Pattern PATTERN_JAVA_TRANSLATE = Pattern.compile("I18N.get\\(\"(.+?)\"\\)");
  public static final Pattern PATTERN_JAVA_CN_TRANSLATE = Pattern.compile("I18N.getByChinese\\(\"(.+?)\"\\)");
  public static final Pattern PATTERN_HTML_TRANSLATE = Pattern.compile("I18N.getByChinese\\(\"(.+?)\"\\)");
  public static final Pattern PATTERN_JS_TRANSLATE = Pattern.compile("top.FETOP.\\$.i18n.prop\\('(.+?)'\\)");

  public static final String JAVA_CN_FORMART = "I18N.getByChinese(\"%s\")";
  public static final String JSP_CN_FORMART = "<%%=I18N.getByChinese(\"%s\")%%>";
  public static final String HTML_CN_FORMART = "${I18N.getByChinese(\"%s\")}";

  public static final String JAVA_FORMART = "I18N.get(\"%s\")";
  public static final String JSP_FORMART = "<%%=I18N.get(\"%s\")%%>";
  public static final String JS_FORMART = "top.FETOP.$.i18n.prop('%s')";
  public static final String HTML_FORMART = "I18N.get('%s')";

  public static final SimpleDateFormat SDF_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
