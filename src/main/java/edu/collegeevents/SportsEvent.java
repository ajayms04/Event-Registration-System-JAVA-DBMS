package edu.collegeevents;

import java.time.LocalDate;
import java.time.LocalTime;

public class SportsEvent extends Event {
    public SportsEvent(int id, String name, LocalDate date, LocalTime start, LocalTime end, String venue, int capacity,
                 String details, String description, LocalDate deadline, String status) {
        super(id,name,date,start,end,venue,capacity,details,description,deadline,status);
    }
    @Override public String getEventType() { return "Sports"; }
}
