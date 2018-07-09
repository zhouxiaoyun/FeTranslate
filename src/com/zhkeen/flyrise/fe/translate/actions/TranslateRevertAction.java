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

public class TranslateRevertAction extends EditorAction {

  public TranslateRevertAction() {
    super(new TranslateRevertHandler(new ActionHandler() {
      @Override
      public void handleResult(Editor editor, PluginUtil pluginUtil, TranslateResultModel model,
          String fileType, int editType, String message) {
        try {
          final SelectionModel selectionModel = editor.getSelectionModel();
          final int selectionStart = selectionModel.getSelectionStart();

          if (editType == 1 && model != null) {
            String newText = selectionModel.getSelectedText()
                .replace(String.format("get(\"%s\")", model.getCode()),
                    String.format("getByChinese(\"%s\")", model.getCn()));
            if (newText.length() > 0) {
              EditorModificationUtil.deleteSelectedText(editor);
              EditorModificationUtil.insertStringAtCaret(editor, newText);
              selectionModel.setSelection(selectionStart, selectionStart + newText.length());
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      @Override
      public void handleError(Editor editor, String errMessage) {
        Application app = ApplicationManager.getApplication();
        app.invokeLater(() -> {
          JOptionPane.showMessageDialog(null, errMessage, "FE企业运营管理平台", JOptionPane.ERROR_MESSAGE);
        });
      }
    }));
  }
}