package com.zhkeen.flyrise.fe.FeTranslate.ProjectComponent;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.zhkeen.flyrise.fe.FeTranslate.Utils.TranslatePluginManager;
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
        return "com.zhkeen.flyrise.fe.FeTranslate.ProjectComponent.TranslateComponent";
    }

    @Override
    public void projectOpened() {
    }

    @Override
    public void projectClosed() {
    }
}
