package org.example.Control;

import java.time.LocalDate;

public class AttendenceRecord {
    boolean didAttend;
    LocalDate date;
    Student student;

    public AttendenceRecord(boolean didAttend, LocalDate date, Student student){
        this.didAttend = didAttend;
        this.date = date;
        this.student = student;
    }
}
