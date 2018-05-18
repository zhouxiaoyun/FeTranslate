package com.zhkeen.flyrise.fe.translate.utils;

import com.intellij.openapi.vfs.VirtualFile;
import com.zhkeen.flyrise.fe.translate.model.JdbcConnectionModel;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginUtil {

  private final Logger logger = LoggerFactory.getLogger(PluginUtil.class);

  private boolean needMultiLanguage = false;
  private String defaultLanguage;
  private Map<String, String> supportLanguageMap;
  private DbUtil dbUtil;

  public PluginUtil(VirtualFile baseDir) {
    try {
      if (FileUtil.existsFeProjectFile(baseDir)) {
        needMultiLanguage = true;
      } else {
        needMultiLanguage = false;
      }
      this.defaultLanguage = "ZH";
      this.supportLanguageMap = Constants.ALL_LANGUAGE_MAP;
      JdbcConnectionModel jdbcConnectionModel = FileUtil.readJdbcConnectionModel(baseDir);
      this.dbUtil = new DbUtil(jdbcConnectionModel);
    } catch (Exception e) {
      needMultiLanguage = false;
      logger.error(e.getMessage());
    }
  }

  public DbUtil getDbUtil() {
    return dbUtil;
  }

  public String getDefaultLanguage() {
    return defaultLanguage;
  }

  public boolean isNeedMultiLanguage() {
    return needMultiLanguage;
  }

  public Map<String, String> getSupportLanguageMap() {
    return supportLanguageMap;
  }
}
