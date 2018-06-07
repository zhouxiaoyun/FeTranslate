package com.zhkeen.flyrise.fe.translate.model;

import java.sql.Date;
import java.util.Map;

/**
 * 翻译模型
 */
public class TranslateResultModel {

  private int id;
  private String code;
  private String cn;
  private String tw;
  private String us;
  private String updateTime;
  private String unitCode;
  private String userId;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCn() {
    return cn;
  }

  public void setCn(String cn) {
    this.cn = cn;
  }

  public String getTw() {
    return tw;
  }

  public void setTw(String tw) {
    this.tw = tw;
  }

  public String getUs() {
    return us;
  }

  public void setUs(String us) {
    this.us = us;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public String getUnitCode() {
    return unitCode;
  }

  public void setUnitCode(String unitCode) {
    this.unitCode = unitCode;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
