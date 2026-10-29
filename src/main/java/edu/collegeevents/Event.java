package edu.collegeevents;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Event implements Validatable {
    private int eventId;
    private String eventName;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String venue;
    private int capacity;
    private String categoryDetails;
    private String description;
    private LocalDate registrationDeadline;
    private String status;

    protected Event(int eventId, String eventName, LocalDate eventDate, LocalTime startTime, LocalTime endTime,
                    String venue, int capacity, String categoryDetails, String description,
                    LocalDate registrationDeadline, String status) {
        this.eventId=eventId; this.eventName=eventName; this.eventDate=eventDate; this.startTime=startTime;
        this.endTime=endTime; this.venue=venue; this.capacity=capacity; this.categoryDetails=categoryDetails;
        this.description=description; this.registrationDeadline=registrationDeadline; this.status=status;
    }
    public int getEventId(){return eventId;} public void setEventId(int v){eventId=v;}
    public String getEventName(){return eventName;} public void setEventName(String v){eventName=v;}
    public LocalDate getEventDate(){return eventDate;} public void setEventDate(LocalDate v){eventDate=v;}
    public LocalTime getStartTime(){return startTime;} public void setStartTime(LocalTime v){startTime=v;}
    public LocalTime getEndTime(){return endTime;} public void setEndTime(LocalTime v){endTime=v;}
    public String getVenue(){return venue;} public void setVenue(String v){venue=v;}
    public int getCapacity(){return capacity;} public void setCapacity(int v){capacity=v;}
    public String getCategoryDetails(){return categoryDetails;} public void setCategoryDetails(String v){categoryDetails=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public LocalDate getRegistrationDeadline(){return registrationDeadline;} public void setRegistrationDeadline(LocalDate v){registrationDeadline=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public abstract String getEventType();
    @Override public boolean validate(){return eventName!=null&&!eventName.isBlank()&&eventDate!=null&&startTime!=null&&endTime!=null
            && endTime.isAfter(startTime)&&venue!=null&&!venue.isBlank()&&capacity>0;}
    public void displayDetails(){System.out.printf("ID: %d | %s | %s | Date: %s | %s-%s | Venue: %s | Capacity: %d | Status: %s%n",
            eventId,eventName,getEventType(),eventDate,startTime,endTime,venue,capacity,status);}
}
