package com.zhkeen.flyrise.fe.FeTranslate.Actions;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.zhkeen.flyrise.fe.FeTranslate.Model.TranslateResultModel;
import com.zhkeen.flyrise.fe.FeTranslate.Utils.DbUtil;
import com.zhkeen.flyrise.fe.FeTranslate.Utils.TranslatePluginManager;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslateHandler extends EditorWriteActionHandler {

    private Logger logger = LoggerFactory.getLogger(TranslateHandler.class);

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
                TranslateResultModel model = dbUtil
                        .findByMessage(manager.getDefaultLanguage(), selectedText,
                                manager.getSupportLanguageMap());
                if (model == null) {
                    model = new TranslateResultModel();
                    model.setId(new Date().getTime());
                    Map<String, String> translateMap = new LinkedHashMap<>();
                    for (String lang : manager.getSupportLanguageMap().keySet()) {
                        if (lang.equals(manager.getDefaultLanguage())) {
                            translateMap.put(lang, selectedText);
                        } else {
                            translateMap.put(lang, "");
                        }
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
