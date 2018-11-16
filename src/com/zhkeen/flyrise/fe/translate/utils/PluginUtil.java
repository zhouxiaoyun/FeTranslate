package com.zhkeen.flyrise.fe.translate.utils;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import com.zhkeen.flyrise.fe.translate.model.JdbcConnectionModel;
import javax.swing.JOptionPane;
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

  public static void handleError(Editor editor, String errMessage) {
    Application app = ApplicationManager.getApplication();
    app.invokeLater(() -> {
      JOptionPane.showMessageDialog(null, errMessage, "FE企业运营管理平台", JOptionPane.ERROR_MESSAGE);
    });
  }

  /**
   * 截断字符串头部及尾部的单引号及双引号
   *
   * @param text 字符串
   * @return 字符串
   */
  public static String trimText(String text) {
    int len = text.length();
    int st = 0;
    if (text.startsWith("'") || text.startsWith("\"")) {
      st = 1;
    }
    if (text.endsWith("'") || text.endsWith("\"")) {
      len = len - 1;
    }
    return ((st > 0) || (len < text.length())) ? text.substring(st, len) : text;
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
