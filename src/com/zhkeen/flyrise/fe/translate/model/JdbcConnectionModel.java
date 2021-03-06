package com.zhkeen.flyrise.fe.translate.model;

/**
 * JDBC连接参数模型
 */
public class JdbcConnectionModel {

  private String driverName;
  private String jdbcUrl;
  private String jdbcUser;
  private String jdbcPassword;
  private int dbType;

  public String getDriverName() {
    return driverName;
  }

  public void setDriverName(String driverName) {
    this.driverName = driverName;
  }

  public String getJdbcUrl() {
    return jdbcUrl;
  }

  public void setJdbcUrl(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }

  public String getJdbcUser() {
    return jdbcUser;
  }

  public void setJdbcUser(String jdbcUser) {
    this.jdbcUser = jdbcUser;
  }

  public String getJdbcPassword() {
    return jdbcPassword;
  }

  public void setJdbcPassword(String jdbcPassword) {
    this.jdbcPassword = jdbcPassword;
  }

  public int getDbType() {
    return dbType;
  }

  public void setDbType(int dbType) {
    this.dbType = dbType;
  }
}
