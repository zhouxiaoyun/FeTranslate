package com.zhkeen.flyrise.fe.translate.model;

import java.sql.Date;
import java.util.Map;

/**
 * 翻译模型
 */
public class TranslateResultModel {

  private String id;
  private Map<String, String> translateMap;
  private Date lastUpdate;
  private String fileType;
  private String fileName;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
