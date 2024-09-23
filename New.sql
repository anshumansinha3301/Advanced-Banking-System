-- Step 1: Creating tables with constraints
CREATE TABLE departments (
    department_id INT PRIMARY KEY,
    department_name VARCHAR(50)
);

CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name VARCHAR(50),
    department_id INT,
    salary DECIMAL(10, 2),
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

-- Step 2: Adding a CHECK constraint to ensure salary > 30000
ALTER TABLE employees
ADD CONSTRAINT chk_salary CHECK (salary > 30000);

-- Step 3: Inserting sample data (ensure salary > 30000 to avoid constraint violation)
INSERT INTO departments (department_id, department_name) VALUES
(1, 'HR'),
(2, 'Sales'),
(3, 'Marketing'),
(4, 'Finance');

INSERT INTO employees (employee_id, name, department_id, salary) VALUES
(1, 'Alice', 1, 50000),
(2, 'Bob', 2, 45000),
(3, 'Charlie', 1, 35000), -- Charlie's salary adjusted to avoid CHECK violation
(4, 'David', 3, 60000),
(5, 'Eve', 4, 70000);

-- Step 4: Displaying the initial state of the tables
SELECT * FROM departments;
SELECT * FROM employees;

-- Step 5: Running the ANY operator query
SELECT name, salary
FROM employees
WHERE salary > ANY (SELECT salary FROM employees WHERE department_id IN (SELECT department_id FROM departments WHERE department_name = 'HR'));

-- Step 6: Running the ALL operator query
SELECT name, salary
FROM employees
WHERE salary > ALL (SELECT salary FROM employees WHERE department_id IN (SELECT department_id FROM departments WHERE department_name = 'HR'));

-- Step 7: Running the IN operator query
SELECT name, department_id
FROM employees
WHERE department_id IN (SELECT department_id FROM departments WHERE department_name IN ('HR', 'Sales', 'Marketing'));

-- Step 8: Running the EXISTS operator query
SELECT name
FROM employees e
WHERE EXISTS (SELECT * FROM departments d WHERE d.department_id = e.department_id AND d.department_name = 'HR');

-- Step 9: Running the NOT EXISTS operator query
SELECT name
FROM employees e
WHERE NOT EXISTS (SELECT * FROM employees emp WHERE emp.employee_id = e.employee_id AND emp.salary > 70000);

-- Step 10: Running the UNION operator query
SELECT name FROM employees
WHERE department_id IN (SELECT department_id FROM departments WHERE department_name = 'HR')
UNION
SELECT name FROM employees
WHERE department_id IN (SELECT department_id FROM departments WHERE department_name = 'Finance');

-- Step 11: Running the INTERSECT-like query using INNER JOIN (since MySQL does not support INTERSECT natively)
SELECT e.name 
FROM employees e
JOIN (SELECT name FROM employees WHERE salary > 50000) emp
ON e.name = emp.name
WHERE e.department_id IN (SELECT department_id FROM departments WHERE department_name = 'HR');

-- Step 12: Displaying the final state of the tables after all operations
SELECT * FROM employees;
