package com.zhkeen.flyrise.fe.translate.actions;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.utils.DbUtil;
import com.zhkeen.flyrise.fe.translate.utils.PatternMatcherUtil;
import com.zhkeen.flyrise.fe.translate.utils.PluginUtil;
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
    String selectedText = PluginUtil.trimText(editor.getSelectionModel().getSelectedText());
    if (selectedText != null && selectedText.length() > 0) {
      pluginUtil = new PluginUtil(editor.getProject().getBaseDir());
      if (pluginUtil.isNeedMultiLanguage()) {
        try {
          DbUtil dbUtil = pluginUtil.getDbUtil();
          String fileName = dataContext.getData(CommonDataKeys.PSI_FILE).toString();
          String fileType = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();

          TranslateResultModel model = null;

          int editType = 1;

          PatternMatcherUtil patternMatcherUtil = new PatternMatcherUtil();
          PatternMatcherUtil.MatherResult matherResult = patternMatcherUtil.match(selectedText);
          if (matherResult.isMatch()) {
            editType = 2;
            model = dbUtil.findByMessage(matherResult.getMatchGroupOne());
            if (model == null && selectedText.length() == 32) { // 防止jsp中的js代码
              model = dbUtil.findByCode(matherResult.getMatchGroupOne());
            }
            if (model == null) {
              PluginUtil.handleError(editor, "翻译条目已不存在！");
              return;
            }
          } else {
            model = dbUtil.findByMessage(selectedText);
            if (model == null && selectedText.length() == 32) {
              model = dbUtil.findByCode(selectedText);
            }
          }

          mHandler.handleResult(editor, pluginUtil, model, fileType, editType, selectedText);
        } catch (Exception e) {
          PluginUtil.handleError(editor, e.getMessage());
        }
      }
    }
  }

}
