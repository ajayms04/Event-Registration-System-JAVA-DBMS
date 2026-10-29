package edu.collegeevents;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final StudentDAO students = new StudentDAO();
    private static final EventDAO events = new EventDAO();
    private static final RegistrationDAO registrations = new RegistrationDAO();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" COLLEGE EVENT REGISTRATION SYSTEM");
        System.out.println("========================================");
        try (var c = DBConnection.getConnection()) {
            System.out.println("Database connection successful.");
        } catch (SQLException ex) {
            System.out.println("Could not connect to MySQL: " + ex.getMessage());
            System.out.println("Check MySQL is running, schema.sql is imported, and DB_URL/DB_USER/DB_PASSWORD are correct.");
            return;
        }
        boolean running = true;
        while (running) {
            menu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> listStudents();
                    case 3 -> updateStudent();
                    case 4 -> deleteStudent();
                    case 5 -> addEvent();
                    case 6 -> listEvents();
                    case 7 -> updateEvent();
                    case 8 -> deleteEvent();
                    case 9 -> registerStudent();
                    case 10 -> registrations.printAll();
                    case 11 -> registrations.printParticipants(readInt("Event ID: "));
                    case 12 -> cancelRegistration();
                    case 0 -> { running = false; System.out.println("Goodbye!"); }
                    default -> System.out.println("Invalid menu option.");
                }
            } catch (SQLException ex) {
                System.out.println("Database operation failed: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                System.out.println("Input error: " + ex.getMessage());
            }
        }
    }

    private static void menu() {
        System.out.println("\n--------------- MAIN MENU ---------------");
        System.out.println("1. Add student\n2. View all students\n3. Update student\n4. Delete student\n" +
                "5. Add event\n6. View all events\n7. Update event\n8. Delete event\n" +
                "9. Register student for event\n10. View all registrations\n11. View participants by event\n" +
                "12. Cancel registration\n0. Exit");
    }

    private static void addStudent() throws SQLException {
        Student s = readStudent(0);
        if (students.add(s)) System.out.println("Student added.");
    }
    private static void listStudents() throws SQLException {
        List<Student> all = students.getAll();
        if (all.isEmpty()) System.out.println("No students found."); else all.forEach(Student::displayDetails);
    }
    private static void updateStudent() throws SQLException {
        int id = readInt("Internal student ID to update: ");
        if (students.findById(id) == null) { System.out.println("Student not found."); return; }
        Student s = readStudent(id);
        System.out.println(students.update(s) ? "Student updated." : "No changes made.");
    }
    private static void deleteStudent() throws SQLException {
        int id = readInt("Internal student ID to delete: ");
        System.out.println(students.delete(id) ? "Student deleted. Related registrations are deleted automatically." : "Student not found.");
    }
    private static Student readStudent(int id) {
        String roll = readText("College roll number: ");
        String name = readText("Name: ");
        String dept = readText("Department: ");
        int year = readInt("Study year (1-6): ");
        String email = readText("Email: ");
        String phone = readText("Phone: ");
        Student s = new Student(id, roll, name, dept, year, email, phone);
        if (!s.validate()) throw new IllegalArgumentException("Please enter non-empty fields, a valid email, and study year from 1 to 6.");
        return s;
    }
    private static void addEvent() throws SQLException {
        Event e = readEvent(0);
        System.out.println(events.add(e) ? "Event added." : "Event was not added.");
    }
    private static void listEvents() throws SQLException {
        List<Event> all = events.getAll();
        if (all.isEmpty()) System.out.println("No events found."); else all.forEach(Event::displayDetails);
    }
    private static void updateEvent() throws SQLException {
        int id = readInt("Event ID to update: ");
        if (events.findById(id) == null) { System.out.println("Event not found."); return; }
        Event e = readEvent(id);
        System.out.println(events.update(e) ? "Event updated." : "No changes made.");
    }
    private static void deleteEvent() throws SQLException {
        int id = readInt("Event ID to delete: ");
        System.out.println(events.delete(id) ? "Event deleted. Related registrations are deleted automatically." : "Event not found.");
    }
    private static Event readEvent(int id) {
        String name = readText("Event name: ");
        String type = readEventType();
        LocalDate date = readDate("Event date (YYYY-MM-DD): ");
        LocalTime start = readTime("Start time (HH:MM): ");
        LocalTime end = readTime("End time (HH:MM): ");
        String venue = readText("Venue: ");
        int cap = readInt("Capacity (informational only): ");
        String details = readText("Category details: ");
        String desc = readText("Description: ");
        String dl = readText("Registration deadline YYYY-MM-DD (or leave blank): ");
        LocalDate deadline = dl.isBlank() ? null : parseDate(dl);
        String status = readText("Status (informational only, e.g. Open): ");
        Event e = switch (type) {
            case "Technical" -> new TechnicalEvent(id, name, date, start, end, venue, cap, details, desc, deadline, status);
            case "Cultural" -> new CulturalEvent(id, name, date, start, end, venue, cap, details, desc, deadline, status);
            case "Sports" -> new SportsEvent(id, name, date, start, end, venue, cap, details, desc, deadline, status);
            default -> new WorkshopEvent(id, name, date, start, end, venue, cap, details, desc, deadline, status);
        };
        if (!e.validate()) throw new IllegalArgumentException("Event fields are required; capacity must be positive and end time must be after start time.");
        return e;
    }
    private static String readEventType() {
        while (true) {
            System.out.println("Event type: 1.Technical  2.Cultural  3.Sports  4.Workshop");
            switch (readInt("Select type: ")) {
                case 1: return "Technical";
                case 2: return "Cultural";
                case 3: return "Sports";
                case 4: return "Workshop";
                default: System.out.println("Choose 1-4.");
            }
        }
    }
    private static void registerStudent() throws SQLException {
        int sid = readInt("Internal student ID (view students first): ");
        int eid = readInt("Event ID (view events first): ");
        if (registrations.register(sid, eid)) System.out.println("Registration added.");
    }
    private static void cancelRegistration() throws SQLException {
        int id = readInt("Registration ID to cancel: ");
        System.out.println(registrations.cancel(id) ? "Registration cancelled." : "Registration not found.");
    }
    private static String readText(String prompt) { System.out.print(prompt); return in.nextLine().trim(); }
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt); String s = in.nextLine().trim();
            try { return Integer.parseInt(s); } catch (NumberFormatException ex) { System.out.println("Enter a whole number."); }
        }
    }
    private static LocalDate readDate(String prompt) { return parseDate(readText(prompt)); }
    private static LocalDate parseDate(String value) {
        try { return LocalDate.parse(value); } catch (DateTimeParseException ex) { throw new IllegalArgumentException("Use date format YYYY-MM-DD."); }
    }
    private static LocalTime readTime(String prompt) {
        String value = readText(prompt);
        try { return LocalTime.parse(value.length() == 5 ? value + ":00" : value); }
        catch (DateTimeParseException ex) { throw new IllegalArgumentException("Use time format HH:MM, e.g. 09:30."); }
    }
}
