package org.example.Control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Student {
    private int id;
    private String name;
    private String surname;
    public List<AttendenceRecord> attendence = new ArrayList<>();
    public Group currentGroup;

    public Student(int id, String name, String surname){
        Random random = new Random();
        this.id = id;
        this.name = name;
        this.surname = surname;
        currentGroup = null;
    }

    public void markAttendence(LocalDate date, boolean didAttend){
        for(AttendenceRecord at : attendence){
            if(at.date == date){
                at.didAttend = didAttend;
                return;
            }
        }
        attendence.add(new AttendenceRecord(didAttend, date));
    }

    public String getName(){ return name;}
    public String getSurname() { return surname; }
    public String getFullName() { return name + " " + surname;}
    public String getIdAsString() {
        String strID = id + "";
        return strID;
    }
    public int getID(){ return id; }

    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
}
