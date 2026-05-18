package org.example.Control;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private int id;
    private List<Student> students; // Must be initialized!

    public Group(String name, int id) {
        this.name = name;
        this.id = id;
        this.students = new ArrayList<>(); // Initialize to avoid NullPointerExceptions
    }

    public String getName() { return name; }
    public List<Student> getStudents() { return students; }
}
