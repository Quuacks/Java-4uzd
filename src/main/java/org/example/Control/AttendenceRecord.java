package org.example.Control;

import java.time.LocalDate;

public class AttendenceRecord {
    boolean didAttend;
    LocalDate date;

    public AttendenceRecord(boolean didAttend, LocalDate date){
        this.didAttend = didAttend;
        this.date = date;
    }
}
