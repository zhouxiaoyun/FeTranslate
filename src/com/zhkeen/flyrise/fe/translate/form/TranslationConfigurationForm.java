package com.zhkeen.flyrise.fe.translate.form;

import com.intellij.ui.EditorTextField;
import com.zhkeen.flyrise.fe.translate.configuration.ConfigurationState;
import com.zhkeen.flyrise.fe.translate.utils.Constants;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class TranslationConfigurationForm {

  private JPanel rootComponent;
  private EditorTextField textAppId = new EditorTextField();
  private EditorTextField textSecretKey = new EditorTextField();

  public TranslationConfigurationForm() {

    GridBagLayout layout = new GridBagLayout();

    rootComponent = new JPanel();
    rootComponent.setPreferredSize(new Dimension(200, 200));
    rootComponent.setLayout(layout);

    JLabel appIdLabel = new JLabel("App ID:");
    layout.setConstraints(appIdLabel,
        new GBC(0, 0).setAnchor(GBC.NORTHEAST)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(0, 0));
    rootComponent.add(appIdLabel);

    layout.setConstraints(textAppId,
        new GBC(1, 0).setFill(GBC.BOTH)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(100, 0));
    rootComponent.add(textAppId);

    JLabel secretKeyLabel = new JLabel("Secret Key:");
    layout.setConstraints(secretKeyLabel,
        new GBC(0, 1).setAnchor(GBC.NORTHEAST)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(0, 0));
    rootComponent.add(secretKeyLabel);

    layout.setConstraints(textSecretKey,
        new GBC(1, 1).setFill(GBC.BOTH)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(100, 0));
    rootComponent.add(textSecretKey);

  }


  public JComponent getRootComponent() {
    return rootComponent;
  }

  public void save(ConfigurationState data) {
    String appId = textAppId.getText();
    String secretKey = textSecretKey.getText();
    data.setAppId(appId);
    data.setSecretKey(secretKey);
  }

  public boolean load(ConfigurationState data) {
    textAppId.setText(data.getAppId());
    textSecretKey.setText(data.getSecretKey());
    return true;
  }

  public boolean isModified(ConfigurationState data) {
    String appId = textAppId.getText();
    String secretKey = textSecretKey.getText();
    return !data.getAppId().equals(appId) || !data.getSecretKey().equals(secretKey);
  }

}
