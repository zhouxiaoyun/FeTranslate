package com.zhkeen.flyrise.fe.translate.utils;

import com.intellij.openapi.vfs.VirtualFile;
import com.zhkeen.flyrise.fe.translate.model.JdbcConnectionModel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;

public class FileUtil {

  public static boolean existsFeProjectFile(VirtualFile baseDir) {
    if (baseDir != null && (baseDir.findFileByRelativePath(Constants.JDBC_PORPERTIES_FILE)
        != null || baseDir.findFileByRelativePath(Constants.JDBC_PORPERTIES_FILE2) != null)) {
      return true;
    } else {
      return false;
    }
  }

  public static List<JdbcConnectionModel> readJdbcConnectionModel(VirtualFile baseDir)
      throws IOException, ConfigurationException {
    VirtualFile propertiesFile = baseDir
        .findFileByRelativePath(Constants.JDBC_PORPERTIES_FILE);
    if (propertiesFile == null) {
      propertiesFile = baseDir
          .findFileByRelativePath(Constants.JDBC_PORPERTIES_FILE2);
    }
    List<JdbcConnectionModel> models = new ArrayList<>();
    if (propertiesFile.exists()) {
      Properties properties = new Properties();
      properties.load(propertiesFile.getInputStream());

      String driverName = properties.getProperty("mssql.jdbc.driver");
      String jdbcUrl = properties.getProperty("mssql.jdbc.url");
      String jdbcUser = properties.getProperty("mssql.jdbc.user");
      String jdbcPassword = properties.getProperty("mssql.jdbc.password");

      if (StringUtils.isNotEmpty(driverName) && StringUtils.isNotEmpty(jdbcUrl) && StringUtils
          .isNotEmpty(jdbcUser) && StringUtils.isNotEmpty(jdbcPassword)) {
        JdbcConnectionModel model = new JdbcConnectionModel();
        model.setDriverName(driverName);
        model.setJdbcUrl(jdbcUrl);
        model.setJdbcUser(jdbcUser);
        model.setJdbcPassword(jdbcPassword);
        model.setDbType(2);
        models.add(model);
      }
      driverName = properties.getProperty("mysql.jdbc.driver");
      jdbcUrl = properties.getProperty("mysql.jdbc.url");
      jdbcUser = properties.getProperty("mysql.jdbc.user");
      jdbcPassword = properties.getProperty("mysql.jdbc.password");
      if (StringUtils.isNotEmpty(driverName) && StringUtils.isNotEmpty(jdbcUrl) && StringUtils
          .isNotEmpty(jdbcUser) && StringUtils.isNotEmpty(jdbcPassword)) {
        JdbcConnectionModel model = new JdbcConnectionModel();
        model.setDriverName(driverName);
        model.setJdbcUrl(jdbcUrl);
        model.setJdbcUser(jdbcUser);
        model.setJdbcPassword(jdbcPassword);
        model.setDbType(3);
        models.add(model);
      }
      driverName = properties.getProperty("oracle.jdbc.driver");
      jdbcUrl = properties.getProperty("oracle.jdbc.url");
      jdbcUser = properties.getProperty("oracle.jdbc.user");
      jdbcPassword = properties.getProperty("oracle.jdbc.password");
      if (StringUtils.isNotEmpty(driverName) && StringUtils.isNotEmpty(jdbcUrl) && StringUtils
          .isNotEmpty(jdbcUser) && StringUtils.isNotEmpty(jdbcPassword)) {
        JdbcConnectionModel model = new JdbcConnectionModel();
        model.setDriverName(driverName);
        model.setJdbcUrl(jdbcUrl);
        model.setJdbcUser(jdbcUser);
        model.setJdbcPassword(jdbcPassword);
        model.setDbType(1);
        models.add(model);
      }
    } else {
      throw new FileNotFoundException("没有找到数据库配置文件！");
    }
    return models;
  }

}
