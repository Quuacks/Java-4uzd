package org.example.Control;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private int id;
    private String name;
    private String surname;
    public List<AttendenceRecord> attendence = new ArrayList<>();

    public Student(){
        id = -1;
        name = "empty";
        surname = "empty";
    }

    public Student(int id, String name, String surname){
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public String getName(){ return name;}
    public String getSurname() { return surname; }
}
