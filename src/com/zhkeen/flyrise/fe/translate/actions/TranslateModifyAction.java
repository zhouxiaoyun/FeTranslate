package com.zhkeen.flyrise.fe.translate.actions;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import com.zhkeen.flyrise.fe.translate.form.TranslateForm;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.utils.PluginUtil;
import java.awt.Point;

public class TranslateModifyAction extends EditorAction {

  public TranslateModifyAction() {
    super(new TranslateHandler(new PopupActionHandler()));
  }

  private static class PopupActionHandler implements ActionHandler {

    @Override
    public void handleResult(Editor editor, PluginUtil pluginUtil, TranslateResultModel model,
        String fileType) {
      Application app = ApplicationManager.getApplication();
      app.invokeLater(() -> {
        try {
          pluginUtil.getDbUtil().update(model);
          TranslateForm translateForm = new TranslateForm(editor, pluginUtil, model, fileType);
          translateForm.setVisible(true);
        } catch (Exception e) {
          showErrorBallon(editor, e.getMessage());
        }
      });
    }

    @Override
    public void handleError(Editor editor, String errMessage) {
      Application app = ApplicationManager.getApplication();
      app.invokeLater(() -> {
        showErrorBallon(editor, errMessage);
      });
    }

    private void showErrorBallon(Editor editor, String message) {
      BalloonBuilder builder =
          JBPopupFactory.getInstance()
              .createHtmlTextBalloonBuilder("FE企业运营管理平台", MessageType.ERROR, null);
      Balloon balloon = builder.createBalloon();
      balloon.setTitle(message);
      CaretModel caretModel = editor.getCaretModel();
      Point point = editor.visualPositionToXY(caretModel.getVisualPosition());
      RelativePoint where = new RelativePoint(point);
      balloon.show(where, Balloon.Position.below);
    }
  }
}
