package com.zhkeen.flyrise.fe.translate.actions;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.utils.Constants;
import com.zhkeen.flyrise.fe.translate.utils.DbUtil;
import com.zhkeen.flyrise.fe.translate.utils.PluginUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;

/**
 * 多语言翻译处理
 */
public class TranslateCnHandler extends EditorWriteActionHandler {

  private PluginUtil pluginUtil;

  private final ActionHandler mHandler;

  public TranslateCnHandler(ActionHandler handler) {
    this.mHandler = handler;
  }

  @Override
  public void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext) {
    if (editor == null) {
      return;
    }
    String selectedText = trimText(editor.getSelectionModel().getSelectedText());
    if (selectedText != null && selectedText.length() > 0) {
      pluginUtil = new PluginUtil(editor.getProject().getBaseDir());
      if (pluginUtil.isNeedMultiLanguage()) {
        try {
          DbUtil dbUtil = pluginUtil.getDbUtil();
          String fileName = dataContext.getData(CommonDataKeys.PSI_FILE).toString();
          String fileType = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
          TranslateResultModel model = null;
          int editType = 1;
          String message = selectedText;
          Pattern pattern = null;

          if (".java".equals(fileType) || ".jsp".equals(fileType)) {
            pattern = Constants.PATTERN_JAVA_CN_TRANSLATE;
          } else if (".html".equals(fileType)) {
            pattern = Constants.PATTERN_HTML_TRANSLATE;
          } else if (".js".equals(fileType)) {
            pattern = Constants.PATTERN_JS_TRANSLATE;
          } else {
            mHandler.handleError(editor, "无效的文件类型！");
            return;
          }

          Matcher matcher = pattern.matcher(selectedText);
          if (matcher.find()) {
            editType = 2;
            if (".java".equals(fileType) || ".jsp".equals(fileType)) {
              model = dbUtil.findByMessage(matcher.group(1));
              if (model == null) { // 防止jsp中的js代码
                model = dbUtil.findByCode(matcher.group(1));
              }
            } else {
              model = dbUtil.findByCode(matcher.group(1));
            }
            if (model == null) {
              mHandler.handleError(editor, "翻译条目已不存在！");
              return;
            }
          } else {
            model = dbUtil.findByMessage(selectedText);
            if (model == null) {
              model = dbUtil.findByCode(matcher.group(1));
            }
          }

          mHandler.handleResult(editor, pluginUtil, model, fileType, editType, message);
        } catch (Exception e) {
          mHandler.handleError(editor, e.getMessage());
        }
      }
    }
  }

  /**
   * 截断字符串头部及尾部的单引号及双引号
   * @param text 字符串
   * @return 字符串
   */
  private String trimText(String text) {
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
}
