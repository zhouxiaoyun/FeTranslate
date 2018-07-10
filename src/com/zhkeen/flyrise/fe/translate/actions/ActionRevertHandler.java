package com.zhkeen.flyrise.fe.translate.actions;

import com.intellij.openapi.editor.Editor;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.utils.PluginUtil;
import java.util.List;

public interface ActionRevertHandler {

  void handleResult(Editor editor, List<TranslateResultModel> models);

  void handleError(Editor editor, String errMessage);
}
