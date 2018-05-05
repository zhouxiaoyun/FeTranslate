package com.zhkeen.flyrise.fe.translate.actions;

import com.intellij.openapi.editor.Editor;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.utils.PluginUtil;

public interface ActionHandler {

  void handleResult(Editor editor, PluginUtil pluginUtil, TranslateResultModel model, String fileType);

  void handleError(Editor editor, String errMessage);
}
