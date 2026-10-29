package edu.collegeevents;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    public boolean add(Event e)throws SQLException{
        if(!e.validate())throw new IllegalArgumentException("Invalid event details.");
        String sql="INSERT INTO events(event_name,event_type,event_date,start_time,end_time,venue,capacity,category_details,description,registration_deadline,status) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){
            bind(p,e);return p.executeUpdate()==1;
        }
    }
    public List<Event> getAll()throws SQLException{
        List<Event> list=new ArrayList<>();try(Connection c=DBConnection.getConnection();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT * FROM events ORDER BY event_date,event_id")){while(r.next())list.add(fromRow(r));}return list;
    }
    public Event findById(int id)throws SQLException{
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM events WHERE event_id=?")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?fromRow(r):null;}}
    }
    public boolean update(Event e)throws SQLException{
        if(!e.validate())throw new IllegalArgumentException("Invalid event details.");
        String sql="UPDATE events SET event_name=?,event_type=?,event_date=?,start_time=?,end_time=?,venue=?,capacity=?,category_details=?,description=?,registration_deadline=?,status=? WHERE event_id=?";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){bind(p,e);p.setInt(12,e.getEventId());return p.executeUpdate()==1;}
    }
    public boolean delete(int id)throws SQLException{try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM events WHERE event_id=?")){p.setInt(1,id);return p.executeUpdate()==1;}}
    private void bind(PreparedStatement p,Event e)throws SQLException{
        p.setString(1,e.getEventName());p.setString(2,e.getEventType());p.setDate(3,Date.valueOf(e.getEventDate()));p.setTime(4,Time.valueOf(e.getStartTime()));p.setTime(5,Time.valueOf(e.getEndTime()));p.setString(6,e.getVenue());p.setInt(7,e.getCapacity());p.setString(8,e.getCategoryDetails());p.setString(9,e.getDescription());if(e.getRegistrationDeadline()==null)p.setNull(10,Types.DATE);else p.setDate(10,Date.valueOf(e.getRegistrationDeadline()));p.setString(11,e.getStatus());
    }
    private Event fromRow(ResultSet r)throws SQLException{
        int id=r.getInt("event_id");String type=r.getString("event_type");String name=r.getString("event_name");LocalDate date=r.getDate("event_date").toLocalDate();LocalTime start=r.getTime("start_time").toLocalTime();LocalTime end=r.getTime("end_time").toLocalTime();String venue=r.getString("venue");int cap=r.getInt("capacity");String details=r.getString("category_details");String desc=r.getString("description");Date d=r.getDate("registration_deadline");LocalDate deadline=d==null?null:d.toLocalDate();String status=r.getString("status");
        return switch(type){case "Technical"->new TechnicalEvent(id,name,date,start,end,venue,cap,details,desc,deadline,status);case "Cultural"->new CulturalEvent(id,name,date,start,end,venue,cap,details,desc,deadline,status);case "Sports"->new SportsEvent(id,name,date,start,end,venue,cap,details,desc,deadline,status);default->new WorkshopEvent(id,name,date,start,end,venue,cap,details,desc,deadline,status);};
    }
}
