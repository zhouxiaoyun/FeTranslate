package com.zhkeen.flyrise.fe.translate.utils;

import com.google.common.collect.ImmutableMap;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 常量
 */
public class Constants {

  /**
   * Form中控件的边距
   */
  public static final int MARGIN = 8;
  /**
   * 文本域默认的行数
   */
  public static final int TEXTAREA_ROWS = 3;
  /**
   * 文本域默认的列数
   */
  public static final int TEXTAREA_COLS = 60;

  /**
   * JDBC配置文件的路径（相对项目工程
   */
  public static final String JDBC_PORPERTIES_FILE = "properties/jdbc.properties";
  public static final String JDBC_PORPERTIES_FILE2 = "src/main/resources/jdbc.properties";

  /**
   * java匹配规则，用于提取多语言部分
   */
  public static final Pattern PATTERN_JAVA_CN_TRANSLATE = Pattern.compile("I18N.getByChinese\\(\"(.+?)\"\\)");
  /**
   * HTML匹配规则，用于提取多语言部分
   */
  public static final Pattern PATTERN_HTML_TRANSLATE = Pattern.compile("I18N.getByChinese\\(\"(.+?)\"\\)");
  /**
   * JS匹配规则，用于提取多语言部分
   */
  public static final Pattern PATTERN_JS_TRANSLATE = Pattern.compile("top.FETOP.\\$.i18n.prop\\('(.+?)'\\)");

  public static final String JAVA_CN_FORMART = "I18N.getByChinese(\"%s\")";
  public static final String JSP_CN_FORMART = "<%%=I18N.getByChinese(\"%s\")%%>";
  public static final String HTML_CN_FORMART = "${I18N.getByChinese(\"%s\")}";
  public static final String JS_FORMART = "top.FETOP.$.i18n.prop('%s')";

  public static final SimpleDateFormat SDF_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
