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
import com.zhkeen.flyrise.fe.translate.utils.TransApi;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslateHandler extends EditorWriteActionHandler {

  private static final String APP_ID = "20180503000153011";
  private static final String SECURITY_KEY = "_MRqRxWs1i75bfvXg4kU";
  ;

  private PluginUtil pluginUtil;

  private final ActionHandler mHandler;

  public TranslateHandler(ActionHandler handler) {
    this.mHandler = handler;
  }

  @Override
  public void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext) {
    if (editor == null) {
      return;
    }
    String selectedText = editor.getSelectionModel().getSelectedText();
    if (selectedText != null && selectedText.length() > 0) {
      pluginUtil = new PluginUtil(editor.getProject().getBaseDir());
      if (pluginUtil.isNeedMultiLanguage()) {
        try {
          DbUtil dbUtil = pluginUtil.getDbUtil();
          String fileName = dataContext.getData(CommonDataKeys.PSI_FILE).toString().toLowerCase();
          String fileType = fileName.substring(fileName.lastIndexOf('.'));

          TranslateResultModel model;
          Matcher m = Constants.PATTERN_TRANSLATE.matcher(selectedText);
          if (m.find()) {
            model = dbUtil
                .findById(Long.parseLong(m.group()), pluginUtil.getSupportLanguageMap());
          } else {
            model = dbUtil
                .findByMessage(pluginUtil.getDefaultLanguage(), selectedText,
                    pluginUtil.getSupportLanguageMap());
          }
          if (model == null) {
            TransApi transApi = new TransApi(APP_ID, SECURITY_KEY);
            model = new TranslateResultModel();
            model.setId(new Date().getTime() * 1000 + new Random().nextInt(1000));
            Map<String, String> translateMap = new LinkedHashMap<>();
            for (String lang : pluginUtil.getSupportLanguageMap().keySet()) {
              if ("zh".equals(lang)) {
                translateMap.put(lang, selectedText);
              } else {
                translateMap.put(lang, transApi.getTransResult(selectedText, "auto", lang));
              }
            }
            model.setTranslateMap(translateMap);
          }
          mHandler.handleResult(editor, pluginUtil, model, fileType);
        } catch (Exception e) {
          mHandler.handleError(editor, e.getMessage());
        }
      }
    }

  }
}
