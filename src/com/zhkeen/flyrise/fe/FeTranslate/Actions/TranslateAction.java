package com.zhkeen.flyrise.fe.FeTranslate.Actions;

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
import com.zhkeen.flyrise.fe.FeTranslate.Form.TranslateForm;
import com.zhkeen.flyrise.fe.FeTranslate.Model.TranslateResultModel;

import java.awt.*;

public class TranslateAction extends EditorAction {

    public TranslateAction() {
        super(new TranslateHandler(new PopupActionHandler()));
    }

    private static class PopupActionHandler implements ActionHandler {

        @Override
        public void handleResult(Editor editor, TranslateResultModel model) {
            Application app = ApplicationManager.getApplication();
            app.invokeLater(() -> {
                TranslateForm resultDialog = new TranslateForm(editor, model);
//        int langSize = model.getTranslateMap().size();
//        int width = Constants.MARGIN * 4 + Constants.LABEL_WIDTH + Constants.TEXTAREA_WIDTH;
//        int height = Constants.MARGIN * (langSize + 1) + Constants.LABEL_HEIGHT
//            + Constants.TEXTAREA_HEIGHT * langSize;
//        resultDialog.setBounds(0, 0, width, height);
                resultDialog.setVisible(true);
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
