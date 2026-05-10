package org.example.UI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class attendenceTab extends UI_Tab{
    @Override
    Tab createTab() {
        DatePicker datePicker = new DatePicker();
        Button markBtn = new Button("Mark Attendance");

        VBox layout = new VBox(10, datePicker, markBtn);
        layout.setPadding(new Insets(10));

        Tab aTab = new Tab(super.name, layout);

        return aTab;
    }

    public attendenceTab(String name){
        super(name);
    }
}
