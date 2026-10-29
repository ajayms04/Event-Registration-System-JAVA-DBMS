package edu.collegeevents;

import java.sql.*;

public class RegistrationDAO {
    public boolean register(int studentId, int eventId) throws SQLException {
        // Duplicate registrations are prevented by the database unique constraint.
        // Deadline, event status, and capacity are intentionally not enforced.
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement("INSERT INTO registrations(student_id,event_id) VALUES(?,?)")) {
            p.setInt(1, studentId);
            p.setInt(2, eventId);
            return p.executeUpdate() == 1;
        } catch (SQLException ex) {
            // MySQL error 1062 means a UNIQUE constraint was violated.
            if (ex.getErrorCode() == 1062) {
                throw new SQLException("This student is already registered for this event.", ex.getSQLState(), ex.getErrorCode(), ex);
            }
            throw ex;
        }
    }

    public void printAll() throws SQLException {
        String sql = "SELECT r.registration_id,r.registration_date,s.roll_no,s.name,e.event_name,e.event_type " +
                "FROM registrations r JOIN students s ON r.student_id=s.student_id " +
                "JOIN events e ON r.event_id=e.event_id ORDER BY r.registration_id";
        try (Connection c = DBConnection.getConnection(); Statement s = c.createStatement(); ResultSet r = s.executeQuery(sql)) {
            System.out.println("\n--- Registrations ---");
            boolean found = false;
            while (r.next()) {
                found = true;
                System.out.printf("Registration ID: %d | Roll: %s | Student: %s | Event: %s (%s) | Registered: %s%n",
                        r.getInt(1), r.getString(3), r.getString(4), r.getString(5), r.getString(6), r.getTimestamp(2));
            }
            if (!found) System.out.println("No registrations found.");
        }
    }

    public void printParticipants(int eventId) throws SQLException {
        String sql = "SELECT e.event_name,s.roll_no,s.name,s.department,s.email FROM registrations r " +
                "JOIN students s ON r.student_id=s.student_id JOIN events e ON r.event_id=e.event_id " +
                "WHERE e.event_id=? ORDER BY s.name";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, eventId);
            try (ResultSet r = p.executeQuery()) {
                boolean found = false;
                while (r.next()) {
                    if (!found) System.out.println("\nParticipants for " + r.getString("event_name") + ":");
                    found = true;
                    System.out.printf("%s | %s | %s | %s%n", r.getString("roll_no"), r.getString("name"), r.getString("department"), r.getString("email"));
                }
                if (!found) System.out.println("No participants found (or event ID does not exist).");
            }
        }
    }

    public boolean cancel(int registrationId) throws SQLException {
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM registrations WHERE registration_id=?")) {
            p.setInt(1, registrationId);
            return p.executeUpdate() == 1;
        }
    }
}
