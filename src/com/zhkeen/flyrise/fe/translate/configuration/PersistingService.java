package com.zhkeen.flyrise.fe.translate.configuration;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "feTranslate",
    storages = @Storage("feTranslate.xml")
)
public class PersistingService implements PersistentStateComponent<ConfigurationState> {

  private ConfigurationState state;

  public static PersistingService getInstance(Project project) {
    return ServiceManager.getService(project, PersistingService.class);
  }

  @Nullable
  @Override
  public ConfigurationState getState() {
    return state;
  }

  @Override
  public void loadState(@NotNull ConfigurationState configurationState) {
    state = configurationState;
  }

  @Override
  public void noStateLoaded() {
    state = new ConfigurationState();
    state.setAppId("20180503000153011");
    state.setSecretKey("_MRqRxWs1i75bfvXg4kU");
  }
}
