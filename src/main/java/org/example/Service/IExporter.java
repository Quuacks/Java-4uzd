package org.example.Service;

import org.example.Control.Student;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IExporter {
    void exportData(File file, List<Student> students, Map<LocalDate, Set<Integer>> attendanceData);
    void importData(File file, List<Student> studentList, Map<LocalDate, Set<Integer>> attendance);
}
