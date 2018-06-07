package com.zhkeen.flyrise.fe.translate.utils;

import com.intellij.openapi.vfs.VirtualFile;
import com.zhkeen.flyrise.fe.translate.model.JdbcConnectionModel;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginUtil {

  private final Logger logger = LoggerFactory.getLogger(PluginUtil.class);

  private boolean needMultiLanguage = false;
  private DbUtil dbUtil;
  private TransApi transApi;

  public PluginUtil(VirtualFile baseDir) {
    try {
      if (FileUtil.existsFeProjectFile(baseDir)) {
        needMultiLanguage = true;
      } else {
        needMultiLanguage = false;
      }
      JdbcConnectionModel jdbcConnectionModel = FileUtil.readJdbcConnectionModel(baseDir);
      this.dbUtil = new DbUtil(jdbcConnectionModel);
      this.transApi = new TransApi("20180503000153011", "_MRqRxWs1i75bfvXg4kU");
    } catch (Exception e) {
      needMultiLanguage = false;
      logger.error(e.getMessage());
    }
  }

  public DbUtil getDbUtil() {
    return dbUtil;
  }

  public boolean isNeedMultiLanguage() {
    return needMultiLanguage;
  }

  public TransApi getTransApi() {
    return transApi;
  }

  public void setTransApi(TransApi transApi) {
    this.transApi = transApi;
  }
}
