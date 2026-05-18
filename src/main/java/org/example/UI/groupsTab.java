package org.example.UI;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.Control.Group;
import org.example.Control.Student;

import java.lang.invoke.VolatileCallSite;
import java.util.ArrayList;
import java.util.List;

public class groupsTab extends UI_Tab{

    ObservableList<Student> students;

    ObservableList<Group> groups = FXCollections.observableArrayList();
    TableView<Group> groupsTableView = new TableView<>();

    int groupID = 0;

    @Override
    public Tab createTab() {

        TextField groupName = new TextField();
        groupName.setPromptText("Group name");

        Button createBtn = new Button("Create Group");
        createBtn.setOnAction(e -> CreateGroup(groupName.getText()));

        groupsTableView.setItems(groups);

        TableColumn<Group, String> nameCol = new TableColumn<>("Group Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Group, Void> deleteCol = new TableColumn<>("Delete group");
        deleteCol.setCellFactory(param -> new TableCell<>(){
            private Button btn = new Button("Delete");
            {
                btn.setOnAction(e -> DeleteGroup(getTableView().getItems().get(getIndex())));
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

        TableColumn<Group, Void> editCol = new TableColumn<>("Edit");
        editCol.setCellFactory(param -> new TableCell<>(){
            private final Button btn = new Button("Edit");

            {
                btn.setOnAction(event -> OnButtonClick(getTableView().getItems().get(getIndex())));
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

        groupsTableView.getColumns().addAll(nameCol, editCol, deleteCol);

        VBox layout = new VBox(10, groupName, createBtn, groupsTableView);
        layout.setPadding(new Insets(10));

        Tab gTab = new Tab(super.name, layout);

        return gTab;
    }
    void DeleteGroup(Group group){
        groups.remove(group);
        groupsTableView.refresh();
    }

    void OnButtonClick(Group group){
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Edit group: " + group.getName());

        Label label = new Label(group.getName());

        VBox currentStudentsBox = new VBox(5);
        Label currentLabel = new Label("Current Students");
        ListView<Student> currentListView = new ListView<>();

        ObservableList<Student> currentStudents = FXCollections.observableArrayList(group.getStudents());
        currentListView.setItems(currentStudents);

        Button removeBtn = new Button("Remove");

        currentStudentsBox.getChildren().addAll(currentLabel, currentListView, removeBtn);

        VBox availableStudentBox = new VBox(5);
        Label availableLabel = new Label("Available Students: ");
        ListView<Student> availableListView = new ListView<>();

        ObservableList<Student> availableStudents = FXCollections.observableArrayList();
        for(Student s : students){
            if(!group.getStudents().contains(s) && s.currentGroup == null){
                availableStudents.add(s);
            }
        }
        availableListView.setItems(availableStudents);

        Button addBtn = new Button("Add Selected");

        availableStudentBox.getChildren().addAll(availableLabel, availableListView, addBtn);

        var cellFactory = new javafx.util.Callback<ListView<Student>, ListCell<Student>>(){
            @Override
            public ListCell<Student> call(ListView<Student> param){
                return new ListCell<>(){
                    @Override
                    protected void updateItem(Student item, boolean empty){
                        super.updateItem(item,empty);
                        if(empty || item == null){
                            setText(null);
                        }
                        else{
                            setText(item.getName() + " " + item.getSurname());
                        }
                    }
                };
            }
        };
        currentListView.setCellFactory(cellFactory);
        availableListView.setCellFactory(cellFactory);

        addBtn.setOnAction(e -> {
            Student selected = availableListView.getSelectionModel().getSelectedItem();
            if(selected != null && selected.currentGroup == null){
                selected.currentGroup = group;
                group.getStudents().add(selected);
                currentStudents.add(selected);
                availableStudents.remove(selected);
                groupsTableView.refresh();
            }
        });

        removeBtn.setOnAction(e ->{
            Student selected = currentListView.getSelectionModel().getSelectedItem();
            if(selected != null && selected.currentGroup != null){
                selected.currentGroup = null;
                group.getStudents().remove(selected);
                currentStudents.remove(selected);
                availableStudents.add(selected);
                groupsTableView.refresh();
            }
        });

        HBox listsContainer = new HBox(15, currentStudentsBox, availableStudentBox);
        listsContainer.setPrefHeight(300);

        VBox layout = new VBox(10, label, listsContainer);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 500, 300);

        popup.setScene(scene);

        popup.showAndWait();
    }

    public groupsTab(String name, ObservableList<Student> students){
        super(name);
        this.students = students;
    }

    public void CreateGroup(String name){
        groups.add(new Group(name, ++groupID));
    }
}
