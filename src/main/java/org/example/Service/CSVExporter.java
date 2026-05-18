package org.example.Service;

import org.example.Control.Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CSVExporter implements IExporter {

    @Override
    public void exportData(File file, List<Student> students, Map<LocalDate, Set<Integer>> attendanceData) {
        File target = AttendanceIOHelper.ensureExtension(file, ".csv");
        List<LocalDate> dates = AttendanceIOHelper.sortedDates(attendanceData);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(target), StandardCharsets.UTF_8))) {
            writer.write('\ufeff');
            writeRow(writer, buildHeader(dates));

            for (Student student : students) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(student.getID()));
                row.add(student.getName());
                row.add(student.getSurname());
                for (LocalDate date : dates) {
                    boolean present = AttendanceIOHelper.isPresent(attendanceData, date, student.getID());
                    row.add(AttendanceIOHelper.formatPresent(present));
                }
                writeRow(writer, row);
            }
        } catch (IOException e) {
            throw new RuntimeException("Nepavyko eksportuoti CSV: " + e.getMessage(), e);
        }
    }

    @Override
    public void importData(File file, List<Student> studentList, Map<LocalDate, Set<Integer>> attendance) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return;
            }
            if (headerLine.startsWith("\ufeff")) {
                headerLine = headerLine.substring(1);
            }

            List<String> headers = parseRow(headerLine);
            if (headers.size() < 3) {
                throw new RuntimeException("Neteisinga CSV antraštė. Tikėtasi: ID,Vardas,Pavardė,[datos...]");
            }

            List<LocalDate> dateColumns = new ArrayList<>();
            for (int i = 3; i < headers.size(); i++) {
                LocalDate date = AttendanceIOHelper.parseDateHeader(headers.get(i));
                if (date != null) {
                    dateColumns.add(date);
                }
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                List<String> cells = parseRow(line);
                if (cells.size() < 3) {
                    continue;
                }

                int id = Integer.parseInt(cells.get(0).trim());
                String name = cells.get(1).trim();
                String surname = cells.get(2).trim();

                Student student = AttendanceIOHelper.findById(studentList, id);
                if (student == null) {
                    student = new Student(id, name, surname);
                    studentList.add(student);
                } else {
                    student.setName(name);
                    student.setSurname(surname);
                }

                for (int i = 0; i < dateColumns.size(); i++) {
                    int cellIndex = i + 3;
                    if (cellIndex >= cells.size()) {
                        break;
                    }
                    boolean present = AttendanceIOHelper.parsePresent(cells.get(cellIndex));
                    AttendanceIOHelper.setPresent(attendance, dateColumns.get(i), id, present);
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Nepavyko importuoti CSV: " + e.getMessage(), e);
        }
    }

    private List<String> buildHeader(List<LocalDate> dates) {
        List<String> header = new ArrayList<>();
        header.add("ID");
        header.add("Vardas");
        header.add("Pavardė");
        for (LocalDate date : dates) {
            header.add(date.toString());
        }
        return header;
    }

    private void writeRow(BufferedWriter writer, List<String> cells) throws IOException {
        for (int i = 0; i < cells.size(); i++) {
            if (i > 0) {
                writer.write(',');
            }
            writer.write(escape(cells.get(i)));
        }
        writer.newLine();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private List<String> parseRow(String line) {
        List<String> cells = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (inQuotes) {
                if (ch == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(ch);
                }
            } else if (ch == '"') {
                inQuotes = true;
            } else if (ch == ',') {
                cells.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
        cells.add(current.toString());
        return cells;
    }
}
