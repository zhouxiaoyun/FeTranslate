package com.zhkeen.flyrise.fe.translate.utils;

import java.util.regex.Matcher;

public class PatternMatcherUtil {

  public MatherResult match(String text) {
    Matcher matcher = Constants.PATTERN_JAVA_CN_TRANSLATE.matcher(text);
    if (matcher.find()) {
      return new MatherResult(true, matcher.group(1));
    } else {
      matcher = Constants.PATTERN_HTML_TRANSLATE.matcher(text);
      if (matcher.find()) {
        return new MatherResult(true, matcher.group(1));
      } else {
        matcher = Constants.PATTERN_JS_TRANSLATE.matcher(text);

        if (matcher.find()) {
          return new MatherResult(true, matcher.group(1));
        } else {
          return new MatherResult(false, "");
        }
      }
    }
  }

  public class MatherResult {

    private boolean match;
    private String matchGroupOne;

    public MatherResult(boolean match, String matchGroupOne) {
      this.match = match;
      this.matchGroupOne = matchGroupOne;
    }

    public boolean isMatch() {
      return match;
    }

    public void setMatch(boolean match) {
      this.match = match;
    }

    public String getMatchGroupOne() {
      return matchGroupOne;
    }

    public void setMatchGroupOne(String matchGroupOne) {
      this.matchGroupOne = matchGroupOne;
    }
  }

}
