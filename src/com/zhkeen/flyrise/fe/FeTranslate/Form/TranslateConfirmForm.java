package com.zhkeen.flyrise.fe.FeTranslate.Form;

import com.intellij.openapi.editor.Editor;
import com.intellij.ui.components.JBScrollPane;
import com.zhkeen.flyrise.fe.FeTranslate.Model.TranslateResultModel;
import com.zhkeen.flyrise.fe.FeTranslate.Utils.Constants;
import com.zhkeen.flyrise.fe.FeTranslate.Utils.TranslatePluginManager;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslateConfirmForm extends JDialog implements ActionListener {

  private Logger logger = LoggerFactory.getLogger(TranslateConfirmForm.class);
  private Map<String, JTextArea> controlls = new HashMap<>();

  public TranslateConfirmForm(Editor editor, TranslateResultModel model) {
    super(JOptionPane.getRootFrame(), "FE企业运营管理平台", true);

    TranslatePluginManager manager = TranslatePluginManager.getInstance();
    Map<String, String> translateMap = model.getTranslateMap();
    Map<String, String> supportLanguageMap = manager.getSupportLanguageMap();

    Container container = getContentPane();

    JLabel idLabel = new JLabel("ID:");
    idLabel.setBounds(Constants.MARGIN, Constants.MARGIN, Constants.LABEL_WIDTH,
        Constants.LABEL_HEIGHT);
    container.add(idLabel);

    JLabel idValueLabel = new JLabel(String.valueOf(model.getId()));
    idValueLabel.setBounds(Constants.MARGIN * 2 + Constants.LABEL_WIDTH, Constants.MARGIN,
        Constants.LABEL_ID_WIDTH, Constants.LABEL_HEIGHT);
    container.add(idValueLabel);

    int i = 1;
    for (String lang : translateMap.keySet()) {
      JLabel label = new JLabel(supportLanguageMap.get(lang));
      label.setBounds(Constants.MARGIN, Constants.MARGIN * (i + 1) + Constants.LABEL_HEIGHT,
          Constants.LABEL_WIDTH,
          Constants.LABEL_HEIGHT);
      container.add(label);
      JTextArea area = new JTextArea(translateMap.get(lang), Constants.TEXTAREA_ROWS,
          Constants.TEXTAREA_COLS);
      area.setLineWrap(true);
      area.setWrapStyleWord(false);
      controlls.put(lang, area);

      JBScrollPane pane = new JBScrollPane(area);
      pane.setBounds(Constants.MARGIN * 2 + Constants.LABEL_ID_WIDTH,
          Constants.MARGIN * (i + 1) + Constants.LABEL_HEIGHT + Constants.TEXTAREA_HEIGHT * (i
              - 1), Constants.TEXTAREA_WIDTH, Constants.TEXTAREA_HEIGHT);
      container.add(pane);
    }

    JButton button = new JButton("确定");
    button.setBounds((Constants.MARGIN * 4 + Constants.LABEL_WIDTH + Constants.TEXTAREA_WIDTH
            - Constants.BUTTON_WIDTH) / 2,
        Constants.MARGIN * (i + 1) + Constants.LABEL_HEIGHT + Constants.TEXTAREA_HEIGHT * (i
            - 1), Constants.BUTTON_WIDTH, Constants.BUTTON_HIGHT);
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          for (String lang : translateMap.keySet()) {
            translateMap.put(lang, controlls.get(lang).getText().trim());
          }
          manager.getDbUtil().update(model);
          setVisible(false);
          dispose();
        } catch (Exception e1) {
          logger.error(e1.getMessage());
        }
      }
    });
    setResizable(true);
    setLocationRelativeTo(getOwner());
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    pack();

    addEscapeListener(this);
  }

  public void actionPerformed(ActionEvent actionEvent) {
    setVisible(false);
    dispose();
  }

  private void addEscapeListener(final JDialog dialog) {
    ActionListener escListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dialog.setVisible(false);
      }
    };

    dialog.getRootPane().registerKeyboardAction(escListener,
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

}
