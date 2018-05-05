package com.zhkeen.flyrise.fe.translate.form;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.ui.components.JBScrollPane;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.utils.Constants;
import com.zhkeen.flyrise.fe.translate.utils.PluginUtil;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslateForm extends JDialog implements ActionListener {

  private Logger logger = LoggerFactory.getLogger(TranslateForm.class);
  private Map<String, JTextArea> controlls = new HashMap<>();

  public TranslateForm(Editor editor, PluginUtil pluginUtil, TranslateResultModel model,
      String fileType) {
    super(JOptionPane.getRootFrame(), "FE企业运营管理平台", true);

    Map<String, String> translateMap = model.getTranslateMap();
    Map<String, String> supportLanguageMap = pluginUtil.getSupportLanguageMap();

    Container panel = getContentPane();
    GridBagLayout layout = new GridBagLayout();
    panel.setLayout(layout);

    JLabel idLabel = new JLabel("ID:");
    layout.setConstraints(idLabel,
        new GBC(0, 0).setAnchor(GBC.NORTHEAST)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(0, 0));
    panel.add(idLabel);

    JLabel idValueLabel = new JLabel(String.valueOf(model.getId()));
    layout.setConstraints(idValueLabel,
        new GBC(1, 0).setFill(GBC.BOTH)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(100, 0));
    panel.add(idValueLabel);

    int i = 1;
    for (String lang : translateMap.keySet()) {
      JLabel label = new JLabel(supportLanguageMap.get(lang) + ":");
      layout.setConstraints(label,
          new GBC(0, i).setAnchor(GBC.NORTHEAST)
              .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
              .setWeight(0, 100));
      panel.add(label);

      JTextArea area = new JTextArea(translateMap.get(lang), Constants.TEXTAREA_ROWS,
          Constants.TEXTAREA_COLS);
      area.setLineWrap(true);
      area.setWrapStyleWord(false);
      controlls.put(lang, area);

      JBScrollPane scrollPane = new JBScrollPane(area);
      layout.setConstraints(scrollPane,
          new GBC(1, i).setFill(GBC.BOTH)
              .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
              .setWeight(100, 100));
      panel.add(scrollPane);
      i++;
    }

    JButton button = new JButton("确定");
    layout.setConstraints(button,
        new GBC(0, i, 2, 1).setFill(GBC.CENTER)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(100, 0));
    panel.add(button);
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          for (String lang : translateMap.keySet()) {
            translateMap.put(lang, controlls.get(lang).getText().trim());
          }
          pluginUtil.getDbUtil().update(model);

          setVisible(false);
          dispose();
        } catch (Exception e1) {
          logger.error(e1.getMessage());
        }
      }
    });
    setResizable(false);
    addEscapeListener(this);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    pack();
    setLocationToCenter();
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

  private void setLocationToCenter() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = this.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    int x = (screenSize.width - frameSize.width) / 2;
    int y = (screenSize.height - frameSize.height) / 2;
    this.setLocation(x, y);
  }
}
