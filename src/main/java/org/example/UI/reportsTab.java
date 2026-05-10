package org.example.UI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class reportsTab extends UI_Tab{
    @Override
    Tab createTab() {
        DatePicker fromDate = new DatePicker();
        DatePicker toDate = new DatePicker();
        Button generateBtn = new Button("Generate PDF");

        VBox layout = new VBox(10, fromDate, toDate, generateBtn);
        layout.setPadding(new Insets(10));

        Tab rTab = new Tab(super.name, layout);

        return rTab;
    }

    public reportsTab(String name){
        super(name);
    }
}
