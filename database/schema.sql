CREATE DATABASE IF NOT EXISTS college_event_db;
USE college_event_db;

CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    roll_no VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    study_year INT NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    event_name VARCHAR(120) NOT NULL,
    event_type VARCHAR(20) NOT NULL,
    event_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    venue VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    category_details VARCHAR(150),
    description VARCHAR(500),
    registration_deadline DATE,
    status VARCHAR(20) DEFAULT 'Open',
    CONSTRAINT chk_event_type CHECK (event_type IN ('Technical','Cultural','Sports','Workshop'))
);

CREATE TABLE IF NOT EXISTS registrations (
    registration_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    event_id INT NOT NULL,
    registration_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_student_event UNIQUE (student_id, event_id),
    CONSTRAINT fk_registration_student FOREIGN KEY (student_id) REFERENCES students(student_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_registration_event FOREIGN KEY (event_id) REFERENCES events(event_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- Seed data is inserted only when the relevant tables are empty.
INSERT INTO students (roll_no, name, department, study_year, email, phone)
SELECT '24CS101','Arun Kumar','CSE',2,'arun@example.com','9876500001'
WHERE NOT EXISTS (SELECT 1 FROM students WHERE roll_no='24CS101');
INSERT INTO students (roll_no, name, department, study_year, email, phone)
SELECT '24EC105','Priya Sharma','ECE',2,'priya@example.com','9876500002'
WHERE NOT EXISTS (SELECT 1 FROM students WHERE roll_no='24EC105');
INSERT INTO students (roll_no, name, department, study_year, email, phone)
SELECT '23IT203','Karthik Raj','IT',3,'karthik@example.com','9876500003'
WHERE NOT EXISTS (SELECT 1 FROM students WHERE roll_no='23IT203');
INSERT INTO students (roll_no, name, department, study_year, email, phone)
SELECT '25CS110','Divya Nair','CSE',1,'divya@example.com','9876500004'
WHERE NOT EXISTS (SELECT 1 FROM students WHERE roll_no='25CS110');

INSERT INTO events (event_name,event_type,event_date,start_time,end_time,venue,capacity,category_details,description,registration_deadline,status)
SELECT 'CodeSprint','Technical','2026-12-10','09:00:00','16:00:00','Main Seminar Hall',100,'Coding contest','A campus coding challenge.','2026-12-05','Open'
WHERE NOT EXISTS (SELECT 1 FROM events WHERE event_name='CodeSprint');
INSERT INTO events (event_name,event_type,event_date,start_time,end_time,venue,capacity,category_details,description,registration_deadline,status)
SELECT 'Dance Mania','Cultural','2026-12-12','14:00:00','18:00:00','Open Auditorium',50,'Dance competition','A cultural stage event.','2026-12-08','Open'
WHERE NOT EXISTS (SELECT 1 FROM events WHERE event_name='Dance Mania');
INSERT INTO events (event_name,event_type,event_date,start_time,end_time,venue,capacity,category_details,description,registration_deadline,status)
SELECT 'Football Cup','Sports','2026-12-15','08:00:00','17:00:00','College Ground',40,'Football tournament','Inter-department football.','2026-12-10','Open'
WHERE NOT EXISTS (SELECT 1 FROM events WHERE event_name='Football Cup');
INSERT INTO events (event_name,event_type,event_date,start_time,end_time,venue,capacity,category_details,description,registration_deadline,status)
SELECT 'AI Workshop','Workshop','2026-12-18','10:00:00','13:00:00','Lab Block 2',60,'Artificial Intelligence','Introductory AI workshop.','2026-12-14','Open'
WHERE NOT EXISTS (SELECT 1 FROM events WHERE event_name='AI Workshop');

INSERT INTO registrations (student_id,event_id)
SELECT s.student_id,e.event_id FROM students s CROSS JOIN events e
WHERE s.roll_no='24CS101' AND e.event_name='CodeSprint'
AND NOT EXISTS (SELECT 1 FROM registrations r WHERE r.student_id=s.student_id AND r.event_id=e.event_id);
INSERT INTO registrations (student_id,event_id)
SELECT s.student_id,e.event_id FROM students s CROSS JOIN events e
WHERE s.roll_no='24EC105' AND e.event_name='AI Workshop'
AND NOT EXISTS (SELECT 1 FROM registrations r WHERE r.student_id=s.student_id AND r.event_id=e.event_id);
