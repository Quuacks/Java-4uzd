package org.example.UI;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.Control.Student;

import java.util.ArrayList;
import java.util.List;


public class UI extends Application {



    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        UI_Tab studentTab = new studentTab("student tab");

        Tab groupsTab = new Tab("Groups", createGroupsPane());
        Tab attendanceTab = new Tab("Attendance", createAttendancePane());
        Tab reportsTab = new Tab("Reports", createReportsPane());

        tabPane.getTabs().addAll(studentTab.createTab(), groupsTab, attendanceTab, reportsTab);

        Scene scene = new Scene(tabPane, 800, 600);
        stage.setTitle("Student Registration System");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createGroupsPane() {
        TextField groupName = new TextField();
        groupName.setPromptText("Group name");

        Button createBtn = new Button("Create Group");

        VBox layout = new VBox(10, groupName, createBtn);
        layout.setPadding(new Insets(10));
        return layout;
    }

    private VBox createAttendancePane() {
        DatePicker datePicker = new DatePicker();
        Button markBtn = new Button("Mark Attendance");

        VBox layout = new VBox(10, datePicker, markBtn);
        layout.setPadding(new Insets(10));
        return layout;
    }

    private VBox createReportsPane() {
        DatePicker fromDate = new DatePicker();
        DatePicker toDate = new DatePicker();
        Button generateBtn = new Button("Generate PDF");

        VBox layout = new VBox(10, fromDate, toDate, generateBtn);
        layout.setPadding(new Insets(10));
        return layout;
    }

    public static void launchApp(){
        launch();
    }
}
