# Project Proposal: College Event Registration System

## Objective
Build a Java console application connected to MySQL through JDBC to maintain student records, campus events, and event registrations.

## Scope
- Student CRUD (create, read, update, delete)
- Event CRUD
- Register students for events, view registrations/participants, cancel registrations
- Sample data to make initial demonstrations easier
- No graphical/web interface

## Data model
### students
`student_id` (PK, auto increment), `roll_no` (unique), `name`, `department`, `study_year`, `email` (unique), `phone`.

### events
`event_id` (PK, auto increment), `event_name`, `event_type`, `event_date`, `start_time`, `end_time`, `venue`, `capacity`, `category_details`, `description`, `registration_deadline`, `status`.

### registrations
`registration_id` (PK, auto increment), `student_id` (FK), `event_id` (FK), `registration_date`.

Relationships: one student can have many registration rows; one event can have many registration rows. `registrations` connects students and events. Deleting a student/event deletes related registration rows through `ON DELETE CASCADE`.

## Class relationship sketch
```text
                  Validatable (interface)
                    ^                 ^
                    |                 |
                 Student        Event (abstract)
                                      ^
                  +-------------------+-------------------+
                  |         |          |                  |
            TechnicalEvent CulturalEvent SportsEvent WorkshopEvent

StudentDAO  ---> Student
EventDAO    ---> Event and its subclasses
RegistrationDAO ---> Registration data in database
Main ---> calls DAOs and displays the console menu
DBConnection ---> opens JDBC connections
```

## Seven OOP concepts and where they appear
1. **Classes and objects**: model classes and instances created by the menu flow.
2. **Encapsulation**: model fields are private and accessed through getters/setters.
3. **Inheritance**: four concrete event categories inherit from abstract `Event`.
4. **Polymorphism**: `Event` variables hold different subclasses and overridden `getEventType()` methods are dispatched at runtime.
5. **Abstraction**: `Event` centralizes common fields/validation and declares abstract `getEventType()`.
6. **Interface**: `Validatable` defines `validate()`, implemented by `Student` and `Event`.
7. **Exception handling**: `SQLException` and invalid input are caught in the menu; try-with-resources closes JDBC resources.

## Deliberate simplifications
Registration deadlines, event status, and event capacity are not enforced; those fields are informational. Duplicate registrations are prevented by a database UNIQUE constraint on `(student_id, event_id)`. The database also enforces foreign keys, unique roll numbers, and unique email addresses.
