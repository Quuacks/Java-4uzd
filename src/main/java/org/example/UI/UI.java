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

    private TableView<Student> studentTable = new TableView<>();
    private ObservableList<Student> students = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        Tab studentsTab = new Tab("Students", createStudentsPane());
        Tab groupsTab = new Tab("Groups", createGroupsPane());
        Tab attendanceTab = new Tab("Attendance", createAttendancePane());
        Tab reportsTab = new Tab("Reports", createReportsPane());

        tabPane.getTabs().addAll(studentsTab, groupsTab, attendanceTab, reportsTab);

        Scene scene = new Scene(tabPane, 800, 600);
        stage.setTitle("Student Registration System");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createStudentsPane() {
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField surnameField = new TextField();
        surnameField.setPromptText("Surname");

        Button addButton = new Button("Add Student");
        addButton.setOnAction(e -> createStudent(nameField.getText(), surnameField.getText(), 0));

        HBox form = new HBox(10, nameField, surnameField, addButton);

        studentTable.setItems(students);

        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Student, String> surnameCol = new TableColumn<>("Surname");
        surnameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSurname()));

        studentTable.getColumns().addAll(nameCol, surnameCol);

        VBox layout = new VBox(10, form, studentTable);
        layout.setPadding(new Insets(10));
        return layout;
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

    void createStudent(String name, String surname, int id){
        Student newStudent = new Student(id, name, surname);
        students.add(newStudent);
        studentTable.setItems(students);
    }

    public static void launchApp(){
        launch();
    }
}
