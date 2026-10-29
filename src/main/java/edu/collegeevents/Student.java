package edu.collegeevents;

public class Student implements Validatable {
    private int studentId;
    private String rollNo;
    private String name;
    private String department;
    private int studyYear;
    private String email;
    private String phone;

    public Student(int studentId, String rollNo, String name, String department, int studyYear, String email, String phone) {
        this.studentId = studentId; this.rollNo = rollNo; this.name = name; this.department = department;
        this.studyYear = studyYear; this.email = email; this.phone = phone;
    }
    public Student(String rollNo, String name, String department, int studyYear, String email, String phone) {
        this(0, rollNo, name, department, studyYear, email, phone);
    }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public int getStudyYear() { return studyYear; }
    public void setStudyYear(int studyYear) { this.studyYear = studyYear; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    @Override public boolean validate() {
        return notBlank(rollNo) && notBlank(name) && notBlank(department) && studyYear >= 1 && studyYear <= 6
                && email != null && email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$") && notBlank(phone);
    }
    private boolean notBlank(String value) { return value != null && !value.trim().isEmpty(); }
    public void displayDetails() {
        System.out.printf("ID: %d | Roll No: %s | Name: %s | Dept: %s | Year: %d | Email: %s | Phone: %s%n",
                studentId, rollNo, name, department, studyYear, email, phone);
    }
}
