package com.example.fushionplugin;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ex.ProjectEx;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
public class TitleConfiguration implements Configurable  {

    private TitleSettingComponent settingComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "RAU Title Format";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return settingComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        settingComponent = new TitleSettingComponent();
        return settingComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        TitleSettingState state = TitleSettingState.getInstance();
        return !settingComponent.getProjectFormat().equals(state.projectFormat) || !settingComponent.getFileFormat().equals(state.fileFormat);
    }

    @Override
    public void apply() {
        TitleSettingState.getInstance().fileFormat = settingComponent.getFileFormat();
        TitleSettingState.getInstance().projectFormat = settingComponent.getProjectFormat();

        triggerTitleRefresh();
    }

    private void triggerTitleRefresh() {
        Project firstProject = ProjectManager.getInstance().getOpenProjects()[0];
        if (firstProject instanceof ProjectEx) {
            String oldName = firstProject.getName();
            ((ProjectEx) firstProject).setProjectName("");
            ((ProjectEx) firstProject).setProjectName(oldName);
        }
    }

    @Override
    public void reset() {
        TitleSettingState state = TitleSettingState.getInstance();
        settingComponent.setProjectFormatText(state.projectFormat);
        settingComponent.setFileFormatText(state.fileFormat);
    }

    @Override
    public void disposeUIResources() {
        settingComponent = null;
    }

}
