package com.zhkeen.flyrise.fe.translate.actions;

import static com.zhkeen.flyrise.fe.translate.utils.Constants.HTML_FORMART;
import static com.zhkeen.flyrise.fe.translate.utils.Constants.JAVA_FORMART;
import static com.zhkeen.flyrise.fe.translate.utils.Constants.JSP_FORMART;
import static com.zhkeen.flyrise.fe.translate.utils.Constants.JS_FORMART;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.utils.Constants;
import com.zhkeen.flyrise.fe.translate.utils.PluginUtil;
import java.awt.Point;
import java.sql.SQLException;
import java.util.regex.Matcher;

public class TranslateReplaceAction extends EditorAction {

  public TranslateReplaceAction() {
    super(new TranslateHandler(new ActionHandler() {
      @Override
      public void handleResult(Editor editor, PluginUtil pluginUtil, TranslateResultModel model,
          String fileType) {
        try {
          pluginUtil.getDbUtil().update(model);
          final SelectionModel selectionModel = editor.getSelectionModel();
          final int selectionStart = selectionModel.getSelectionStart();
          String oldText = selectionModel.getSelectedText();
          Matcher m = Constants.PATTERN_TRANSLATE.matcher(oldText);
          if (!m.find()) {
            String newText = "";
            switch (fileType) {
              case ".java":
                newText = String.format(JAVA_FORMART, model.getId());
                break;
              case ".jsp":
                newText = String.format(JSP_FORMART, model.getId());
                break;
              case ".js":
                newText = String.format(JS_FORMART, model.getId());
                break;
              case ".html":
                newText = String.format(HTML_FORMART, model.getId());
                break;
              default:
                break;
            }
            EditorModificationUtil.deleteSelectedText(editor);
            EditorModificationUtil.insertStringAtCaret(editor, newText);
            selectionModel.setSelection(selectionStart, selectionStart + newText.length());
          }
        } catch (SQLException e) {
          e.printStackTrace();
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void handleError(Editor editor, String errMessage) {
        Application app = ApplicationManager.getApplication();
        app.invokeLater(() -> {BalloonBuilder builder =
            JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder("FE企业运营管理平台", MessageType.ERROR, null);
          Balloon balloon = builder.createBalloon();
          balloon.setTitle(errMessage);
          CaretModel caretModel = editor.getCaretModel();
          Point point = editor.visualPositionToXY(caretModel.getVisualPosition());
          RelativePoint where = new RelativePoint(point);
          balloon.show(where, Balloon.Position.below);
        });
      }
    }));
  }

//  @Override
//  public void update(Editor editor, Presentation presentation, DataContext dataContext) {
//    String fileName = dataContext.getData(CommonDataKeys.PSI_FILE).toString().toLowerCase();
//    if (fileName.endsWith(".java") || fileName.endsWith(".html") || fileName.endsWith(".js")) {
//      super.update(editor, presentation, dataContext);
//    } else {
//      presentation.setVisible(false);
//    }
//  }

}
