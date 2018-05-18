package com.zhkeen.flyrise.fe.translate.model;

import java.sql.Date;
import java.util.Map;

public class TranslateResultModel {

  private String id;
  private String isJs;
  private Map<String, String> translateMap;
  private Date lastUpdate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIsJs() {
    return isJs;
  }

  public void setIsJs(String isJs) {
    this.isJs = isJs;
  }

  public Map<String, String> getTranslateMap() {
    return translateMap;
  }

  public void setTranslateMap(Map<String, String> translateMap) {
    this.translateMap = translateMap;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }
}
