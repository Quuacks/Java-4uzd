package org.example.UI;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.Control.Student;

public class studentTab extends UI_Tab{

    private TableView<Student> studentTable = new TableView<>();
    private ObservableList<Student> students = FXCollections.observableArrayList();

    @Override
    Tab createTab(){


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

        Tab sTab = new Tab("Student Tab", layout);

        return sTab;
    }

    void createStudent(String name, String surname, int id){
        Student newStudent = new Student(id, name, surname);
        students.add(newStudent);
        studentTable.setItems(students);
    }

    public studentTab(String name){
        super(name);
    }
}
