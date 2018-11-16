package com.zhkeen.flyrise.fe.translate.actions;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.zhkeen.flyrise.fe.translate.form.TranslateForm;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.utils.Constants;
import com.zhkeen.flyrise.fe.translate.utils.PluginUtil;
import javax.swing.JOptionPane;

public class TranslateJsInHtmlOrJspCnReplaceAction extends EditorAction {

  public TranslateJsInHtmlOrJspCnReplaceAction() {
    super(new TranslateCnHandler(new ActionHandler() {
      @Override
      public void handleResult(Editor editor, PluginUtil pluginUtil, TranslateResultModel model,
          String fileType, int editType, String message) {
        try {
          final SelectionModel selectionModel = editor.getSelectionModel();
          final int selectionStart = selectionModel.getSelectionStart();

          if (editType == 1 && model != null) {
            String newText = String.format(Constants.JS_FORMART, model.getCode());
            if (newText.length() > 0) {
              EditorModificationUtil.deleteSelectedText(editor);
              EditorModificationUtil.insertStringAtCaret(editor, newText);
              selectionModel.setSelection(selectionStart, selectionStart + newText.length());
            }
          } else {
            Application app = ApplicationManager.getApplication();
            app.invokeLater(() -> {
              try {
                TranslateForm translateForm = new TranslateForm(editor, pluginUtil, model, fileType,
                    editType, message);
                translateForm.setVisible(true);
              } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "FE企业运营管理平台",JOptionPane.ERROR_MESSAGE);
              }
            });
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      @Override
      public void handleError(Editor editor, String errMessage) {
        Application app = ApplicationManager.getApplication();
        app.invokeLater(() -> {
          JOptionPane.showMessageDialog(null, errMessage, "FE企业运营管理平台",JOptionPane.ERROR_MESSAGE);
        });
      }
    }));
  }
}
