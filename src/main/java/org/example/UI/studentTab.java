package org.example.UI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.Control.Student;
import org.w3c.dom.Text;

public class studentTab extends UI_Tab{

    private TableView<Student> studentTable = new TableView<>();
    private ObservableList<Student> students;

    int nextID = 0;

    @Override
    Tab createTab(){


        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField surnameField = new TextField();
        surnameField.setPromptText("Surname");

        Button addButton = new Button("Add Student");
        addButton.setOnAction(e -> createStudent(nameField.getText(), surnameField.getText(), ++nextID));

        HBox form = new HBox(10, nameField, surnameField, addButton);

        studentTable.setItems(students);

        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Student, String> surnameCol = new TableColumn<>("Surname");
        surnameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSurname()));

        TableColumn<Student, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdAsString()));

        TableColumn<Student, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(param -> new TableCell<>(){
            private final Button btn = new Button("Edit");

            {
                btn.setOnAction(event -> OnEditClick(getTableView().getItems().get(getIndex())));
            }

                @Override
                protected void updateItem(Void item, boolean empty){
                    super.updateItem(item, empty);

                    if(empty){
                        setGraphic(null);
                    }
                    else{
                        setGraphic(btn);
                    }
            }
        });

        studentTable.getColumns().addAll(nameCol, surnameCol, idCol, actionCol);

        VBox layout = new VBox(10, form, studentTable);
        layout.setPadding(new Insets(10));

        Tab sTab = new Tab("Student Tab", layout);

        return sTab;
    }

    void OnEditClick(Student student){
        OpenEditWindow(student);
    }

    void OpenEditWindow(Student student){
        Stage popup = new Stage();

        Label label = new Label(student.getFullName());

        TextField nameField = new TextField(student.getName());
        TextField surnameField = new TextField(student.getSurname());

        Button saveButton = new Button("Save");
        Button removeButton = new Button("Remove");

        removeButton.setOnAction(e -> RemoveStudent(student, popup));

        saveButton.setOnAction(e -> {
            student.setName(nameField.getText());
            student.setSurname(surnameField.getText());

            studentTable.refresh();

            popup.close();
        });

        VBox layout = new VBox(10, label, nameField, surnameField, removeButton, saveButton);

        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 300, 200);

        popup.setScene(scene);
        popup.setTitle("Edit Student");

        popup.show();
    }
    void RemoveStudent(Student student, Stage popup){
        students.remove(student);
        studentTable.refresh();

        popup.close();
    }

    void createStudent(String name, String surname, int id){
        Student newStudent = new Student(id, name, surname);
        students.add(newStudent);
        studentTable.setItems(students);
    }

    public studentTab(String name, ObservableList<Student> students){
        super(name);
        this.students = students;
    }
}
