package com.zhkeen.flyrise.fe.translate.actions;

import com.intellij.openapi.editor.Editor;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;

public interface ActionHandler {

  void handleResult(Editor editor, TranslateResultModel model);

  void handleError(Editor editor, String errMessage);
}
