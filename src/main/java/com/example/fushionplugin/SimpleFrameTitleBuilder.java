package com.example.fushionplugin;

import com.intellij.openapi.wm.impl.PlatformFrameTitleBuilder;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class SimpleFrameTitleBuilder extends PlatformFrameTitleBuilder {

    TitleSettingState state = TitleSettingState.getInstance();

    @Override
    public String getProjectTitle(@NotNull Project project) {
        String title = state.projectFormat;
        if (title.contains("{DEFAULT}")) {
            title = title.replace("{DEFAULT}", super.getProjectTitle(project));
        }
        if (title.contains("{SIMPLE}")) {
            title = title.replace("{SIMPLE}", project.getName());
        }
        return title;
    }

    @Override
    public String getFileTitle(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        String title = state.fileFormat;

        VirtualFile tempVirtualFile = virtualFile;
        VirtualFile previousVirtualFile = virtualFile;

        while(!tempVirtualFile.getName().equals(project.getName())) {
            previousVirtualFile = tempVirtualFile;
            tempVirtualFile = tempVirtualFile.getParent();
        }

        title = title.replace("{DEFAULT}", "*[" + previousVirtualFile.getName() + "]* - " + super.getFileTitle(project, virtualFile));

        return title;
    }

}
