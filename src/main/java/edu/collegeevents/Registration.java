package edu.collegeevents;

import java.time.LocalDateTime;

public class Registration {
    private int registrationId;
    private int studentId;
    private int eventId;
    private LocalDateTime registrationDate;
    public Registration(int registrationId,int studentId,int eventId,LocalDateTime registrationDate){
        this.registrationId=registrationId;this.studentId=studentId;this.eventId=eventId;this.registrationDate=registrationDate;
    }
    public int getRegistrationId(){return registrationId;} public int getStudentId(){return studentId;}
    public int getEventId(){return eventId;} public LocalDateTime getRegistrationDate(){return registrationDate;}
}
