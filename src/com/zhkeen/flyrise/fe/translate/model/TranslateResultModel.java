package com.zhkeen.flyrise.fe.translate.model;

import java.util.Map;

public class TranslateResultModel {

  private long id;
  private Map<String, String> translateMap;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Map<String, String> getTranslateMap() {
    return translateMap;
  }

  public void setTranslateMap(Map<String, String> translateMap) {
    this.translateMap = translateMap;
  }
}
