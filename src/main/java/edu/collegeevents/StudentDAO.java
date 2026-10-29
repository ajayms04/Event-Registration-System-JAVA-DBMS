package edu.collegeevents;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public boolean add(Student s) throws SQLException {
        if(!s.validate()) throw new IllegalArgumentException("Invalid student details.");
        String sql="INSERT INTO students(roll_no,name,department,study_year,email,phone) VALUES(?,?,?,?,?,?)";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){
            bind(p,s); return p.executeUpdate()==1;
        }
    }
    public List<Student> getAll() throws SQLException {
        List<Student> list=new ArrayList<>();
        try(Connection c=DBConnection.getConnection();Statement st=c.createStatement();ResultSet r=st.executeQuery("SELECT * FROM students ORDER BY student_id")){
            while(r.next()) list.add(fromRow(r));
        } return list;
    }
    public Student findById(int id) throws SQLException {
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM students WHERE student_id=?")){
            p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?fromRow(r):null;}
        }
    }
    public boolean update(Student s) throws SQLException {
        if(!s.validate()) throw new IllegalArgumentException("Invalid student details.");
        String sql="UPDATE students SET roll_no=?,name=?,department=?,study_year=?,email=?,phone=? WHERE student_id=?";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){
            bind(p,s);p.setInt(7,s.getStudentId());return p.executeUpdate()==1;
        }
    }
    public boolean delete(int id) throws SQLException {
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM students WHERE student_id=?")){
            p.setInt(1,id);return p.executeUpdate()==1;
        }
    }
    private void bind(PreparedStatement p,Student s)throws SQLException{
        p.setString(1,s.getRollNo());p.setString(2,s.getName());p.setString(3,s.getDepartment());p.setInt(4,s.getStudyYear());p.setString(5,s.getEmail());p.setString(6,s.getPhone());
    }
    private Student fromRow(ResultSet r)throws SQLException{return new Student(r.getInt("student_id"),r.getString("roll_no"),r.getString("name"),r.getString("department"),r.getInt("study_year"),r.getString("email"),r.getString("phone"));}
}
