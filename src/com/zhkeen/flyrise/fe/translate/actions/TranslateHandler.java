package com.zhkeen.flyrise.fe.translate.actions;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.utils.DbUtil;
import com.zhkeen.flyrise.fe.translate.utils.TransApi;
import com.zhkeen.flyrise.fe.translate.utils.TranslatePluginManager;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslateHandler extends EditorWriteActionHandler {

  private Logger logger = LoggerFactory.getLogger(TranslateHandler.class);
  private static final String APP_ID = "20180503000153011";
  private static final String SECURITY_KEY = "_MRqRxWs1i75bfvXg4kU";

  private final ActionHandler mHandler;

  public TranslateHandler(ActionHandler handler) {
    this.mHandler = handler;
  }

  @Override
  public void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext) {
    if (editor == null) {
      return;
    }

    TranslatePluginManager manager = TranslatePluginManager.getInstance();
    String selectedText = editor.getSelectionModel().getSelectedText();

    if (selectedText != null && selectedText.length() > 0 && manager.isNeedMultiLanguage()) {
      try {
        DbUtil dbUtil = manager.getDbUtil();
        TransApi transApi = new TransApi(APP_ID, SECURITY_KEY);
        TranslateResultModel model = dbUtil
            .findByMessage(manager.getDefaultLanguage(), selectedText,
                manager.getSupportLanguageMap());
        if (model == null) {
          model = new TranslateResultModel();
          model.setId(new Date().getTime() * 1000 + new Random().nextInt(1000));
          Map<String, String> translateMap = new LinkedHashMap<>();
          for (String lang : manager.getSupportLanguageMap().keySet()) {
            translateMap.put(lang, transApi.getTransResult(selectedText, "auto", lang));
          }
          model.setTranslateMap(translateMap);
        }
        mHandler.handleResult(editor, model);
      } catch (Exception e) {
        mHandler.handleError(editor, e.getMessage());
      }
    }
  }
}
