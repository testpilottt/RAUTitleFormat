package com.example.fushionplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(name = "com.roomj.simpletitles.TitleSettingsState", storages = @Storage("SdkSettingsPlugin.xml"))
public class TitleSettingState implements PersistentStateComponent<TitleSettingState>  {

    public String projectFormat = "{DEFAULT}";
    public String fileFormat = "{SIMPLE}";

    public static TitleSettingState getInstance() {
        return ApplicationManager.getApplication().getService(TitleSettingState.class);
    }

    @Nullable
    @Override
    public TitleSettingState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull TitleSettingState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}
