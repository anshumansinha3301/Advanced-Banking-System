-- Creating tables with constraints
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

-- Adding a CHECK constraint
ALTER TABLE employees
ADD CONSTRAINT chk_salary CHECK (salary > 30000);

-- ANY operator
SELECT name, salary
FROM employees
WHERE salary > ANY (SELECT salary FROM employees WHERE department_id IN (SELECT department_id FROM departments WHERE department_name = 'HR'));

-- ALL operator
SELECT name, salary
FROM employees
WHERE salary > ALL (SELECT salary FROM employees WHERE department_id IN (SELECT department_id FROM departments WHERE department_name = 'HR'));

-- IN operator
SELECT name, department_id
FROM employees
WHERE department_id IN (SELECT department_id FROM departments WHERE department_name IN ('HR', 'Sales', 'Marketing'));

-- EXISTS operator
SELECT name
FROM employees e
WHERE EXISTS (SELECT * FROM departments d WHERE d.manager_id = e.employee_id);

-- NOT EXISTS operator
SELECT name
FROM employees e
WHERE NOT EXISTS (SELECT * FROM projects p WHERE p.manager_id = e.employee_id);

-- UNION operator
SELECT name FROM employees
WHERE department_id IN (SELECT department_id FROM departments WHERE department_name = 'HR')
UNION
SELECT name FROM employees
WHERE department_id IN (SELECT department_id FROM departments WHERE department_name = 'Finance');

-- INTERSECT operator (Note: MySQL does not support INTERSECT directly, so we can achieve similar results with INNER JOIN)
SELECT e.name 
FROM employees e
JOIN (SELECT name FROM employees WHERE salary > 50000) emp
ON e.name = emp.name
WHERE e.department_id IN (SELECT department_id FROM departments WHERE department_name = 'HR');
