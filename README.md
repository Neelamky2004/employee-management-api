# Employee Management API

A REST API to add, view, update, delete and search employees. Built with Spring Boot and PostgreSQL.

## Features

- CRUD operations for employees
- Search employees by department
- Input validation (required fields, valid email, joining date not in future)
- Duplicate email check
- Proper error responses (400, 404, 409) using a global exception handler
- Unit tests for the service layer with JUnit 5 and Mockito

## Tech Used

- Java 17
- Spring Boot 3 (Web, Data JPA, Validation)
- PostgreSQL
- Hibernate / JPA
- Maven
- JUnit 5, Mockito
- Postman for API testing

## Project Structure

```
src/main/java/com/neelam/employee
 ├── controller   EmployeeController (REST endpoints)
 ├── service      EmployeeService (business logic)
 ├── repository   EmployeeRepository (JPA)
 ├── model        Employee (entity)
 └── exception    custom exceptions + GlobalExceptionHandler
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/employees` | Get all employees |
| GET | `/api/employees?department=IT` | Get employees by department |
| GET | `/api/employees/{id}` | Get employee by id |
| POST | `/api/employees` | Add new employee |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |

Sample request body:

```json
{
  "name": "Rahul Sharma",
  "email": "rahul@test.com",
  "department": "IT",
  "designation": "Software Engineer",
  "joiningDate": "2025-07-01"
}
```

Sample error response:

```json
{
  "timestamp": "2026-10-02T11:20:15",
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "email": "Email is not valid"
  }
}
```

## How to Run

1. Create a database in PostgreSQL:

```sql
CREATE DATABASE employee_db;
```

2. Set your database username and password in `src/main/resources/application.properties`
   (or set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` environment variables).

3. Run the app:

```bash
mvn spring-boot:run
```

The API will start at `http://localhost:8080`. The `employees` table is created automatically.

Run tests:

```bash
mvn test
```
