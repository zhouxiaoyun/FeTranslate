package com.zhkeen.flyrise.fe.translate.configuration;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.projectImport.ProjectImportBuilder;
import com.zhkeen.flyrise.fe.translate.form.TranslationConfigurationForm;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;

public final class ConfigurationComponent implements ApplicationComponent, Configurable {
	public static final String COMPONENT_NAME = "FeTranslate.ConfigurationComponent";

	public static final String CONFIGURATION_LOCATION;

	static {
		CONFIGURATION_LOCATION = System.getProperty("user.home");
	}

	private TranslationConfigurationForm form;
	private PersistingService instance;

	@Override
	public boolean isModified() {
		return form != null && form.isModified(getState());
	}

	private ConfigurationState getState() {
		return instance.getState();
	}

	@NotNull
	public String getComponentName() {
		return COMPONENT_NAME;
	}

	@Override
	public void initComponent() {
	}

	@Override
	public void disposeComponent() {
	}

	@Override
	public String getDisplayName() {
		return "FeTranslate";
	}

	public Icon getIcon() {
		return null;
	}

	@Override
	public String getHelpTopic() {
		return null;
	}

	@Override
	public JComponent createComponent() {
		if (form == null) {
			form = new TranslationConfigurationForm();
		}
		instance = PersistingService.getInstance(ProjectImportBuilder.getCurrentProject());
		ConfigurationState state = instance.getState();
		form.load(state);

		return form.getRootComponent();
	}

	@Override
	public void apply() throws ConfigurationException {
		if (form != null) {
			form.save(getState());
		}
	}

	@Override
	public void reset() {
		if (form != null) {
			form.load(getState());
		}
	}

	@Override
	public void disposeUIResources() {
		form = null;
	}

}
