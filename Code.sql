-- Creating the Students table
CREATE TABLE Students (
    Student_id INT NOT NULL,
    Student_name VARCHAR(30),
    marks INT CHECK (marks > 35),
    PRIMARY KEY (Student_id)
);

-- Inserting data into the Students table
INSERT INTO Students (Student_id, Student_name, marks) VALUES (1, 'Gunnet', 90);
INSERT INTO Students (Student_id, Student_name, marks) VALUES (2, 'Ahan', 92);
INSERT INTO Students (Student_id, Student_name, marks) VALUES (3, 'Yash', 87);
INSERT INTO Students (Student_id, Student_name, marks) VALUES (4, 'Lavish', 80);
INSERT INTO Students (Student_id, Student_name, marks) VALUES (5, 'Ashish', 90);

-- Selecting data to verify insertion
SELECT * FROM Students;
