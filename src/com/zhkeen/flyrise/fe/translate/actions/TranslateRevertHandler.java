package com.zhkeen.flyrise.fe.translate.actions;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.utils.Constants;
import com.zhkeen.flyrise.fe.translate.utils.DbUtil;
import com.zhkeen.flyrise.fe.translate.utils.PluginUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;

public class TranslateRevertHandler extends EditorWriteActionHandler {

  private PluginUtil pluginUtil;

  private final ActionRevertHandler mHandler;

  public TranslateRevertHandler(ActionRevertHandler handler) {
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
          int editType = 1;
          String message = selectedText;
          Pattern pattern = null;

          if (".java".equals(fileType) || ".jsp".equals(fileType)) {
            pattern = Constants.PATTERN_JAVA_TRANSLATE;
          } else {
            mHandler.handleError(editor, "无效的文件类型！");
            return;
          }

          List<TranslateResultModel> models = new ArrayList<>();
          Matcher matcher = pattern.matcher(message);
          while (matcher.find()) {
            TranslateResultModel model = dbUtil.findByCode(matcher.group(1));
            if (model != null) {
              models.add(model);
            }
          }
          mHandler.handleResult(editor, models);

        } catch (Exception e) {
          mHandler.handleError(editor, e.getMessage());
        }
      }
    }
  }

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
