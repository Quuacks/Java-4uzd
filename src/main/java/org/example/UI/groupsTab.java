package org.example.UI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class groupsTab extends UI_Tab{

    @Override
    public Tab createTab() {

        TextField groupName = new TextField();
        groupName.setPromptText("Group name");

        Button createBtn = new Button("Create Group");

        VBox layout = new VBox(10, groupName, createBtn);
        layout.setPadding(new Insets(10));

        Tab gTab = new Tab(super.name, layout);

        return gTab;
    }

    public groupsTab(String name){
        super(name);
    }
}
