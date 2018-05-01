package com.zhkeen.flyrise.fe.FeTranslate.Actions;

import com.intellij.openapi.editor.Editor;
import com.zhkeen.flyrise.fe.FeTranslate.Model.TranslateResultModel;

public interface ActionHandler {

  void handleResult(Editor editor, TranslateResultModel model);

  void handleError(Editor editor, String errMessage);
}
