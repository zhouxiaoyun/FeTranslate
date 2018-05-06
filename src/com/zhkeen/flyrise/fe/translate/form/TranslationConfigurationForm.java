package com.zhkeen.flyrise.fe.translate.form;

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
  private TextArea textAreaAppId = new TextArea();
  private TextArea textAreaSecretKey = new TextArea();

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

    layout.setConstraints(textAreaAppId,
        new GBC(1, 0).setFill(GBC.BOTH)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(100, 0));
    rootComponent.add(textAreaAppId);

    JLabel secretKeyLabel = new JLabel("Secret Key:");
    layout.setConstraints(secretKeyLabel,
        new GBC(0, 0).setAnchor(GBC.NORTHEAST)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(0, 0));
    rootComponent.add(secretKeyLabel);

    layout.setConstraints(textAreaSecretKey,
        new GBC(1, 0).setFill(GBC.BOTH)
            .setInsets(Constants.MARGIN, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN)
            .setWeight(100, 0));
    rootComponent.add(textAreaSecretKey);

  }


  public JComponent getRootComponent() {
    return rootComponent;
  }

  public void save(ConfigurationState data) {
    String appId = textAreaAppId.getText();
    String secretKey = textAreaSecretKey.getText();
    data.setAppId(appId);
    data.setSecretKey(secretKey);
  }

  public boolean load(ConfigurationState data) {
    textAreaAppId.setText(data.getAppId());
    textAreaSecretKey.setText(data.getSecretKey());
    return true;
  }

  public boolean isModified(ConfigurationState data) {
    String appId = textAreaAppId.getText();
    String secretKey = textAreaSecretKey.getText();
    return !data.getAppId().equals(appId) || !data.getSecretKey().equals(secretKey);
  }

}
