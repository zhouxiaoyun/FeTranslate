package com.zhkeen.flyrise.fe.translate.form;

import com.intellij.openapi.editor.Editor;
import com.zhkeen.flyrise.fe.translate.model.TranslateResultModel;
import com.zhkeen.flyrise.fe.translate.utils.Constants;
import com.zhkeen.flyrise.fe.translate.utils.MD5;
import com.zhkeen.flyrise.fe.translate.utils.PluginUtil;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslateForm extends JDialog implements ActionListener {

  private Logger logger = LoggerFactory.getLogger(TranslateForm.class);
  private JTextField codeTextField;
  private JTextArea cnTextArea;
  private JTextArea twTextArea;
  private JTextArea usTextArea;

  public TranslateForm(Editor editor, PluginUtil pluginUtil, final TranslateResultModel model,
      String fileType, int editType, String message) {
    super(JOptionPane.getRootFrame(), "FE企业运营管理平台", true);

    Container panel = getContentPane();
    GridBagLayout layout = new GridBagLayout();
    panel.setLayout(layout);

    JLabel codeLabel = new JLabel("编码:");
    layout.setConstraints(codeLabel,
        new GBC(0, 0).setAnchor(GBC.NORTHEAST)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(0, 0));
    panel.add(codeLabel);

    codeTextField = new JTextField();
    layout.setConstraints(codeTextField,
        new GBC(1, 0).setFill(GBC.BOTH)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(100, 0));
    if (model != null) {
      codeTextField.setText(model.getCode());
      codeTextField.setEnabled(false);
    } else {
      try {
        codeTextField.setText(MD5.md5(message));
      } catch (Exception e) {
        codeTextField.setText(String.valueOf(new Date().getTime()));
      }
    }
    panel.add(codeTextField);

    JLabel cnLabel = new JLabel("简体中文:");
    layout.setConstraints(cnLabel,
        new GBC(0, 1).setAnchor(GBC.NORTHEAST)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(0, 0));
    panel.add(cnLabel);

    cnTextArea = new JTextArea("", Constants.TEXTAREA_ROWS, Constants.TEXTAREA_COLS);
    layout.setConstraints(cnTextArea,
        new GBC(1, 1).setFill(GBC.BOTH)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(100, 0));
    if (model != null) {
      cnTextArea.setText(model.getCn());
    } else {
      cnTextArea.setText(message);
    }
    cnTextArea.setEnabled(false);
    panel.add(cnTextArea);

    JLabel twLabel = new JLabel("繁体中文:");
    layout.setConstraints(twLabel,
        new GBC(0, 2).setAnchor(GBC.NORTHEAST)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(0, 0));
    panel.add(twLabel);

    twTextArea = new JTextArea("", Constants.TEXTAREA_ROWS, Constants.TEXTAREA_COLS);
    layout.setConstraints(twTextArea,
        new GBC(1, 2).setFill(GBC.BOTH)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(100, 0));
    if (model != null) {
      twTextArea.setText(model.getTw());
    } else {
      try {
        twTextArea.setText(pluginUtil.getTransApi().getTransResult(message, "auto", "cht"));
      } catch (Exception e) {
        JOptionPane
            .showMessageDialog(null, e.getMessage(), "FE企业运营管理平台", JOptionPane.ERROR_MESSAGE);
      }
    }
    panel.add(twTextArea);

    JLabel usLabel = new JLabel("英文:");
    layout.setConstraints(usLabel,
        new GBC(0, 3).setAnchor(GBC.NORTHEAST)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(0, 0));
    panel.add(usLabel);

    usTextArea = new JTextArea("", Constants.TEXTAREA_ROWS, Constants.TEXTAREA_COLS);
    layout.setConstraints(usTextArea,
        new GBC(1, 3).setFill(GBC.BOTH)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(100, 0));
    if (model != null) {
      usTextArea.setText(model.getUs());
    } else {
      try {
        usTextArea.setText(pluginUtil.getTransApi().getTransResult(message, "auto", "en"));
      } catch (Exception e) {
        JOptionPane
            .showMessageDialog(null, e.getMessage(), "FE企业运营管理平台", JOptionPane.ERROR_MESSAGE);
      }
    }
    panel.add(usTextArea);

    JButton button = new JButton("确定");
    layout.setConstraints(button,
        new GBC(0, 4, 2, 1).setFill(GBC.CENTER)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(100, 0));
    panel.add(button);

    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          TranslateResultModel translateResultModel = new TranslateResultModel();
          translateResultModel.setCode(codeTextField.getText());
          translateResultModel.setCn(cnTextArea.getText());
          translateResultModel.setTw(twTextArea.getText());
          translateResultModel.setUs(usTextArea.getText());
          translateResultModel.setUpdateTime(Constants.SDF_TIME.format(new Date()));
          if (model == null) {
            translateResultModel.setUnitCode("1");
            translateResultModel.setUserId("1");
            pluginUtil.getDbUtil().insertTranslate(translateResultModel);
          } else {
            translateResultModel.setId(model.getId());
            translateResultModel.setUnitCode(model.getUnitCode());
            translateResultModel.setUserId(model.getUserId());
            pluginUtil.getDbUtil().updateTranslate(translateResultModel);
          }
          setVisible(false);
          dispose();
        } catch (Exception e1) {
          logger.error(e1.getMessage());
          JOptionPane
              .showMessageDialog(null, e1.getMessage(), "FE企业运营管理平台", JOptionPane.ERROR_MESSAGE);
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
