package com.zhkeen.flyrise.fe.translate.component;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.zhkeen.flyrise.fe.translate.utils.TranslatePluginManager;
import org.jetbrains.annotations.NotNull;

public class TranslateComponent implements ProjectComponent {

  private Project project;

  public TranslateComponent(Project project) {
    this.project = project;
  }

  @Override
  public void initComponent() {
    TranslatePluginManager manager = TranslatePluginManager.getInstance();
    manager.inital(project.getBaseDir());
  }

  @Override
  public void disposeComponent() {
  }

  @Override
  @NotNull
  public String getComponentName() {
    return "com.zhkeen.flyrise.fe.translate.component.TranslateComponent";
  }

  @Override
  public void projectOpened() {
  }

  @Override
  public void projectClosed() {
  }
}
