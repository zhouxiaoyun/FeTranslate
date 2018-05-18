package com.zhkeen.flyrise.fe.translate.utils;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import com.zhkeen.flyrise.fe.translate.model.JdbcConnectionModel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;

public class FileUtil {

  public static boolean existsFeProjectFile(VirtualFile baseDir) {
    if (baseDir != null && baseDir.findFileByRelativePath(Constants.JDBC_PORPERTIES_FILE)
        .exists()) {
      return true;
    } else {
      return false;
    }
  }

  public static JdbcConnectionModel readJdbcConnectionModel(VirtualFile baseDir)
      throws IOException, ConfigurationException {
    VirtualFile propertiesFile = baseDir
        .findFileByRelativePath(Constants.JDBC_PORPERTIES_FILE);
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

        return model;
      }else {
        throw new ConfigurationException("数据库配置文件错误！");
      }
    } else {
      throw new FileNotFoundException("没有找到数据库配置文件！");
    }
  }

}
