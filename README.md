# College Event Registration System (Java + MySQL + JDBC)

A console-based team project demonstrating seven OOP concepts alongside database CRUD and registration workflows.

## Requirements
- JDK 17 or newer
- MySQL 8.x (or compatible version supporting `CHECK` constraints)
- Maven 3.8+

## 1. Create the database
1. Start MySQL.
2. Open MySQL Workbench or the MySQL command line.
3. Run/import `database/schema.sql`. It creates `college_event_db`, its three tables, and sample students, events, and registrations.

The script uses sample/example data. Replace it with your college-approved test data if needed.

## 2. Configure connection credentials
The application defaults to `jdbc:mysql://localhost:3306/college_event_db`, username `root`, and an empty password. Prefer setting environment variables rather than editing source code:

Windows PowerShell (current terminal):
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/college_event_db"
$env:DB_USER="root"
$env:DB_PASSWORD="your_mysql_password"
```

macOS/Linux:
```bash
export DB_URL="jdbc:mysql://localhost:3306/college_event_db"
export DB_USER="root"
export DB_PASSWORD="your_mysql_password"
```

If your local MySQL root account has no password, leave `DB_PASSWORD` empty. Never commit a real password to a public repository.

## 3. Run
From this project folder:
```bash
mvn clean compile exec:java
```
Maven downloads the MySQL JDBC driver on first run. You can also open the folder in IntelliJ IDEA or Eclipse as a Maven project and run `edu.collegeevents.Main`.

## Database model
- `students`: internal auto-increment `student_id` plus unique college `roll_no`, name, department, study year, email, and phone.
- `events`: event details and one of four types: Technical, Cultural, Sports, Workshop.
- `registrations`: links students and events; foreign keys maintain referential integrity.

`student_id` is the database's internal key. `roll_no` is the student's official college roll number. Registration prompts use internal IDs to keep the first version simple; use View Students to see the ID-to-roll-number mapping.

## Scope choices
The program prevents duplicate registrations: a student can register for a particular event only once. This is enforced by the database unique constraint `uq_student_event` on `(student_id, event_id)`, so it remains protected even if another part of the application attempts the same insert. The program does **not** enforce registration deadlines, event status, or event capacity; these fields are informational. Foreign keys also prevent registering a non-existent student or event.

If you imported an earlier version of `schema.sql` and the `registrations` table already exists, `CREATE TABLE IF NOT EXISTS` will not add the new constraint automatically. Run this one-time migration in MySQL Workbench (first make sure no duplicate student/event pairs already exist):

```sql
USE college_event_db;
ALTER TABLE registrations
ADD CONSTRAINT uq_student_event UNIQUE (student_id, event_id);
```

For a fresh setup, simply run the updated `database/schema.sql`.

## Menu
Add/view/update/delete students; add/view/update/delete events; register a student for an event; view registrations; list participants by event; cancel a registration; exit.

## OOP concepts shown
1. Classes and objects: `Student`, `Event` subclasses, `Registration`.
2. Encapsulation: private fields with getters/setters.
3. Inheritance: category-specific event classes extend `Event`.
4. Polymorphism: `Event` references call overridden `getEventType()` / `displayDetails()`.
5. Abstraction: abstract `Event` class defines common event behavior.
6. Interface: `Validatable` is implemented by `Student` and `Event`.
7. Exception handling: `try/catch` for SQL and input errors; try-with-resources for database resources.

See `docs/PROJECT_PROPOSAL.md` and `docs/VIVA_AND_DEMO.md` for the blueprint and viva prep.
