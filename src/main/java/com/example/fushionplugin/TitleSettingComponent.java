package com.example.fushionplugin;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
public class TitleSettingComponent {

    private final JPanel myMainPanel;
    private final JBTextField projectFormatText = new JBTextField();
    private final JBTextField fileFormatText = new JBTextField();

    public TitleSettingComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addComponent(new JBLabel("RAU Title Format."))
                .addComponent(new JBLabel("This tab is to ensure that the plugin is loaded."))
                .addComponent(new JBLabel(" "))
                .addComponent(new JBLabel(" "))
                .addComponent(new JBLabel(" "))
                .addComponent(new JBLabel("Purpose: help RAU"))
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return projectFormatText;
    }

    @NotNull
    public String getProjectFormat() {
        return projectFormatText.getText();
    }

    public void setProjectFormatText(@NotNull String newText) {
        projectFormatText.setText(newText);
    }

    @NotNull
    public String getFileFormat() {
        return fileFormatText.getText();
    }

    public void setFileFormatText(@NotNull String newText) {
        fileFormatText.setText(newText);
    }

}
