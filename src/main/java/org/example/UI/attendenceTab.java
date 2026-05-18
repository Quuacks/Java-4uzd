package org.example.UI;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.Control.Student;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class attendenceTab extends UI_Tab{
    private ObservableList<Student> allStudents;
    private TableView<Student> attendenceTableView = new TableView<>();
    private DatePicker datePicker = new DatePicker();

    private final Map<LocalDate, Set<Integer>> attendenceData;

    @Override
    Tab createTab() {
        datePicker.setValue(LocalDate.now());
        datePicker.setOnAction(e -> reloadAttendenceData());

        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFullName()));

        TableColumn<Student, Boolean> presentCol = new TableColumn<>("Attended");

        presentCol.setCellFactory(CheckBoxTableCell.forTableColumn(presentCol));

        presentCol.setCellValueFactory(data -> {
            Student student = data.getValue();
            LocalDate selectedDate = datePicker.getValue();

            boolean isPresent = attendenceData.containsKey(selectedDate) && attendenceData.get(selectedDate).contains(student.getID());

            SimpleBooleanProperty property = new SimpleBooleanProperty(isPresent);

            property.addListener((obs, wasPresent, isNowPresent) -> {
                Set<Integer> presentStudents = attendenceData.computeIfAbsent(selectedDate, k -> new HashSet<>());

                if(isNowPresent){
                    presentStudents.add(student.getID());
                }
                else{
                    presentStudents.remove(student.getID());
                }
            });

            return property;
        });

        attendenceTableView.setEditable(true);
        attendenceTableView.setItems(allStudents);
        attendenceTableView.getColumns().addAll(nameCol, presentCol);

        HBox topMenu = new HBox(10, new Label("Select Date"), datePicker);
        topMenu.setPadding(new Insets(10));

        VBox layout = new VBox(10, topMenu, attendenceTableView);
        layout.setPadding(new Insets(10));

        return new Tab(super.name, layout);

    }

    void reloadAttendenceData(){
        attendenceTableView.refresh();
    }

    public attendenceTab(String name, ObservableList<Student> students, Map<LocalDate, Set<Integer>> attendenceData){
        super(name);
        this.allStudents = students;
        this.attendenceData = attendenceData;
    }
}
