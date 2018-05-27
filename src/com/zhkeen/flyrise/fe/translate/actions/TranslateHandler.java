package com.zhkeen.flyrise.fe.translate.actions;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.zhkeen.flyrise.fe.translate.configuration.ConfigurationState;
import com.zhkeen.flyrise.fe.translate.configuration.PersistingService;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.utils.Constants;
import com.zhkeen.flyrise.fe.translate.utils.DbUtil;
import com.zhkeen.flyrise.fe.translate.utils.LanguageIdUtil;
import com.zhkeen.flyrise.fe.translate.utils.PluginUtil;
import com.zhkeen.flyrise.fe.translate.utils.TransApi;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import org.jetbrains.annotations.Nullable;

public class TranslateHandler extends EditorWriteActionHandler {

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
    String selectedText = trimText(editor.getSelectionModel().getSelectedText());
    if (selectedText != null && selectedText.length() > 0) {
      pluginUtil = new PluginUtil(editor.getProject().getBaseDir());
      if (pluginUtil.isNeedMultiLanguage()) {
        try {
          DbUtil dbUtil = pluginUtil.getDbUtil();
          String fileName = dataContext.getData(CommonDataKeys.PSI_FILE).toString();
          String fileType = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
          TranslateResultModel model;
          Matcher m = Constants.PATTERN_TRANSLATE.matcher(selectedText);
          if (m.find()) {
            model = dbUtil
                .findById(m.group(), pluginUtil.getSupportLanguageMap());
          } else {
            model = dbUtil
                .findByMessage(pluginUtil.getDefaultLanguage(), selectedText,
                    pluginUtil.getSupportLanguageMap());
          }
          if (model == null) {
            ConfigurationState state = PersistingService.getInstance(editor.getProject())
                .getState();
            TransApi transApi = new TransApi(state.getAppId(), state.getSecretKey());
            model = new TranslateResultModel();
            model.setId(LanguageIdUtil.generateId());

            Map<String, String> translateMap = new LinkedHashMap<>();
            for (String lang : pluginUtil.getSupportLanguageMap().keySet()) {
              if ("ZH".equals(lang)) {
                translateMap.put(lang, selectedText);
              } else {
                translateMap
                    .put(lang, transApi.getTransResult(selectedText, "auto", lang.toLowerCase()));
              }
            }
            model.setTranslateMap(translateMap);
          }
          model.setLastUpdate(new Date(new java.util.Date().getTime()));
          model.setFileType(fileType);
          model.setFileName(fileName);
          mHandler.handleResult(editor, pluginUtil, model, fileType);
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
