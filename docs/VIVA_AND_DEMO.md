# Demo and Viva Preparation

## Suggested 5-minute demo
1. Explain the three tables and their relationships.
2. Choose **View all students** and point out internal `student_id` versus college `roll_no`.
3. Add a student using a unique roll number/email, then view students.
4. View the four seeded events.
5. Register the new student's internal ID for an event; view registrations and participants.
6. Cancel the registration.
7. Demonstrate that a second registration for the same student/event is rejected, while deadline, status, and capacity checks are intentionally not enforced.

## Likely viva questions
**Why have both `student_id` and `roll_no`?**  `student_id` is a stable database primary key used by foreign keys. `roll_no` is the official college identifier and is unique, but it can be changed if the institution's policy requires it.

**Why use JDBC?** JDBC lets Java send SQL statements to MySQL and read the results.

**Why use `PreparedStatement`?** It parameterizes values, helps avoid SQL injection, and handles values safely.

**What is a foreign key?** A constraint that requires a referenced student/event row to exist, preserving referential integrity.

**Where is inheritance?** `TechnicalEvent`, `CulturalEvent`, `SportsEvent`, and `WorkshopEvent` extend `Event`.

**Where is polymorphism?** `EventDAO.getAll()` returns `List<Event>` while each element is an appropriate subclass. Calling `getEventType()` uses the subclass implementation.

**Where is abstraction?** `Event` is abstract and declares `getEventType()` without implementing the category-specific answer.

**Where is the interface?** `Validatable` requires `validate()`, implemented by `Student` and `Event`.

**Where is exception handling?** `Main` catches SQL and validation errors; input parsing catches number/date/time errors; try-with-resources closes connections/statements/result sets.

**Are capacity and deadlines enforced?** No. They are stored and displayed only, intentionally matching the project scope. Event status is also informational. Adding enforcement would require extra checks before inserting a registration.

**How are duplicate registrations prevented?** The `registrations` table has a UNIQUE constraint on `(student_id,event_id)`. MySQL rejects a second registration for the same student and event, and the Java program displays a friendly message.

**What happens when a student/event is deleted?** The foreign key's `ON DELETE CASCADE` removes related registrations.

**Why are DAOs used?** They separate SQL/database operations from the menu and model classes.

## Important setup reminder
Before presenting, import `database/schema.sql`, verify MySQL credentials, and run `mvn clean compile exec:java`. Test the exact commands on the computer that will be used for the demo.
