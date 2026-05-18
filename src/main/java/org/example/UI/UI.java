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

    private ObservableList<Student> students = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        UI_Tab studentTab = new studentTab("Students", students);
        UI_Tab groupTab = new groupsTab("Groups", students);
        UI_Tab attendenceTab = new attendenceTab("Attendance", students);
        UI_Tab reportsTab = new reportsTab("Reports");

        tabPane.getTabs().addAll(studentTab.createTab(), groupTab.createTab(), attendenceTab.createTab(), reportsTab.createTab());

        Scene scene = new Scene(tabPane, 800, 600);
        stage.setTitle("Student Registration System");
        stage.setScene(scene);
        stage.show();
    }

    public static void launchApp(){
        launch();
    }
}
