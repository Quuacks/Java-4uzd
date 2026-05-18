package org.example.UI;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.example.Control.Student;
import org.example.Service.AttendanceIOHelper;
import org.example.Service.CSVExporter;
import org.example.Service.ExcelExporter;
import org.example.Service.IExporter;
import org.example.Service.PDFReportGenerator;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class reportsTab extends UI_Tab {
    private final ObservableList<Student> students;
    private final Map<LocalDate, Set<Integer>> attendanceData;

    private final TableView<Student> reportTable = new TableView<>();
    private final DatePicker fromDate = new DatePicker();
    private final DatePicker toDate = new DatePicker();

    @Override
    public Tab createTab() {
        fromDate.setValue(LocalDate.now().minusDays(7));
        toDate.setValue(LocalDate.now());

        reportTable.setItems(students);
        fromDate.setOnAction(e -> buildColumns());
        toDate.setOnAction(e -> buildColumns());

        Button btnExportCSV = new Button("Eksportuoti CSV");
        Button btnImportCSV = new Button("Importuoti CSV");
        Button btnExportExcel = new Button("Eksportuoti Excel");
        Button btnImportExcel = new Button("Importuoti Excel");
        Button btnExportPDF = new Button("Eksportuoti PDF");

        btnExportCSV.setOnAction(e -> handleExport(new CSVExporter(), "CSV", "*.csv"));
        btnImportCSV.setOnAction(e -> handleImport(new CSVExporter(), "CSV", "*.csv"));
        btnExportExcel.setOnAction(e -> handleExport(new ExcelExporter(), "Excel", "*.xlsx"));
        btnImportExcel.setOnAction(e -> handleImport(new ExcelExporter(), "Excel", "*.xlsx"));
        btnExportPDF.setOnAction(e -> generatePDFReport());

        HBox filterBox = new HBox(10, new Label("Nuo:"), fromDate, new Label("Iki:"), toDate);
        filterBox.setPadding(new Insets(5));

        HBox actionBox = new HBox(10, btnExportCSV, btnImportCSV, btnExportExcel, btnImportExcel, btnExportPDF);
        actionBox.setPadding(new Insets(5));

        buildColumns();

        VBox layout = new VBox(10, filterBox, reportTable, actionBox);
        layout.setPadding(new Insets(10));

        return new Tab(super.name, layout);
    }

    private void handleExport(IExporter manager, String typeName, String extension) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Išsaugoti " + typeName + " duomenis");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(typeName + " failai", extension));
        File file = fc.showSaveDialog(null);
        if (file == null) {
            return;
        }
        try {
            manager.exportData(file, students, attendanceData);
            showInfo("Eksportas sėkmingas", typeName + " failas išsaugotas.");
        } catch (RuntimeException ex) {
            showError("Eksporto klaida", ex.getMessage());
        }
    }

    private void handleImport(IExporter manager, String typeName, String extension) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Pasirinkite " + typeName + " failą importui");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(typeName + " failai", extension));
        File file = fc.showOpenDialog(null);
        if (file == null) {
            return;
        }
        try {
            manager.importData(file, students, attendanceData);
            reportTable.refresh();
            buildColumns();
            showInfo("Importas sėkmingas", typeName + " duomenys įkelti.");
        } catch (RuntimeException ex) {
            showError("Importo klaida", ex.getMessage());
        }
    }

    private void generatePDFReport() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Išsaugoti PDF ataskaitą");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF failai", "*.pdf"));
        File file = fc.showSaveDialog(null);
        if (file == null) {
            return;
        }
        try {
            PDFReportGenerator.createReport(file, students, attendanceData, fromDate.getValue(), toDate.getValue());
            showInfo("PDF sukurtas", "Ataskaita išsaugota pasirinktame intervale.");
        } catch (RuntimeException ex) {
            showError("PDF klaida", ex.getMessage());
        }
    }

    private void buildColumns() {
        reportTable.getColumns().clear();

        TableColumn<Student, String> studentCol = new TableColumn<>("Studentas");
        studentCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFullName()));
        studentCol.setPrefWidth(180);
        reportTable.getColumns().add(studentCol);

        LocalDate start = fromDate.getValue();
        LocalDate end = toDate.getValue();
        if (start == null || end == null || start.isAfter(end)) {
            return;
        }

        LocalDate current = start;
        while (!current.isAfter(end)) {
            final LocalDate dateKey = current;
            TableColumn<Student, String> dateCol = new TableColumn<>(dateKey.toString());
            dateCol.setCellValueFactory(cellData -> {
                int studentId = cellData.getValue().getID();
                boolean attended = AttendanceIOHelper.isPresent(attendanceData, dateKey, studentId);
                return new SimpleStringProperty(AttendanceIOHelper.formatPresent(attended));
            });
            dateCol.setPrefWidth(90);
            reportTable.getColumns().add(dateCol);
            current = current.plusDays(1);
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public reportsTab(String name, ObservableList<Student> students, Map<LocalDate, Set<Integer>> attendanceData) {
        super(name);
        this.students = students;
        this.attendanceData = attendanceData;
    }
}
