package org.example.Service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.Control.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExcelExporter implements IExporter {

    @Override
    public void exportData(File file, List<Student> students, Map<LocalDate, Set<Integer>> attendanceData) {
        File target = AttendanceIOHelper.ensureExtension(file, ".xlsx");
        List<LocalDate> dates = AttendanceIOHelper.sortedDates(attendanceData);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Students");

            Row headerRow = sheet.createRow(0);
            writeCell(headerRow, 0, "ID");
            writeCell(headerRow, 1, "Vardas");
            writeCell(headerRow, 2, "Pavardė");
            for (int i = 0; i < dates.size(); i++) {
                writeCell(headerRow, i + 3, dates.get(i).toString());
            }

            int rowIndex = 1;
            for (Student student : students) {
                Row row = sheet.createRow(rowIndex++);
                writeCell(row, 0, student.getID());
                writeCell(row, 1, student.getName());
                writeCell(row, 2, student.getSurname());

                for (int i = 0; i < dates.size(); i++) {
                    LocalDate date = dates.get(i);
                    boolean present = AttendanceIOHelper.isPresent(attendanceData, date, student.getID());
                    writeCell(row, i + 3, AttendanceIOHelper.formatPresent(present));
                }
            }

            for (int i = 0; i <= dates.size() + 2; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream(target)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            throw new RuntimeException("Nepavyko eksportuoti Excel: " + e.getMessage(), e);
        }
    }

    @Override
    public void importData(File file, List<Student> studentList, Map<LocalDate, Set<Integer>> attendance) {
        try (FileInputStream in = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(in)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return;
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Excel failas neturi antraštės eilutės.");
            }

            List<LocalDate> dateColumns = new ArrayList<>();
            int lastCell = headerRow.getLastCellNum();
            for (int col = 3; col < lastCell; col++) {
                String header = readCellAsString(headerRow.getCell(col));
                LocalDate date = AttendanceIOHelper.parseDateHeader(header);
                if (date != null) {
                    dateColumns.add(date);
                }
            }

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                String idText = readCellAsString(row.getCell(0));
                if (idText.isBlank()) {
                    continue;
                }

                int id = (int) Double.parseDouble(idText);
                String name = readCellAsString(row.getCell(1));
                String surname = readCellAsString(row.getCell(2));

                Student student = AttendanceIOHelper.findById(studentList, id);
                if (student == null) {
                    student = new Student(id, name, surname);
                    studentList.add(student);
                } else {
                    student.setName(name);
                    student.setSurname(surname);
                }

                for (int i = 0; i < dateColumns.size(); i++) {
                    Cell cell = row.getCell(i + 3);
                    boolean present = AttendanceIOHelper.parsePresent(readCellAsString(cell));
                    AttendanceIOHelper.setPresent(attendance, dateColumns.get(i), id, present);
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Nepavyko importuoti Excel: " + e.getMessage(), e);
        }
    }

    private void writeCell(Row row, int column, String value) {
        row.createCell(column).setCellValue(value);
    }

    private void writeCell(Row row, int column, int value) {
        row.createCell(column).setCellValue(value);
    }

    private String readCellAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double numeric = cell.getNumericCellValue();
                if (numeric == Math.floor(numeric)) {
                    yield String.valueOf((long) numeric);
                }
                yield String.valueOf(numeric);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getStringCellValue().trim();
            default -> "";
        };
    }
}
