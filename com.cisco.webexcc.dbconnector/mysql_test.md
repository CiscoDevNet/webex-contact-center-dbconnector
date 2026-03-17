# MySQL Test Notes

## Connection

```java
// Local app run (non-container)
String host = "jdbc:mysql://127.0.0.1:3306/MyTestDb";

// Container app run (Docker/Podman)
// String host = "jdbc:mysql://host.containers.internal:3306/MyTestDb";

String username = "xxx";
String password = "xxx";
```

## Create Test Table

```sql
-- Optional cleanup
DROP TABLE IF EXISTS test_data_types;

-- Table with common MySQL data types
CREATE TABLE test_data_types (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    username        VARCHAR(50) NOT NULL,
    email           VARCHAR(120) NOT NULL UNIQUE,
    age             INT,
    salary          DECIMAL(10,2),
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    rating          FLOAT,
    created_at      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    last_login_at   DATETIME,
    birth_date      DATE,
    notes           TEXT,
    metadata_json   JSON
);
```

## Seed Data (10 Records)

```sql
INSERT INTO test_data_types (
    username,
    email,
    age,
    salary,
    is_active,
    rating,
    created_at,
    last_login_at,
    birth_date,
    notes,
    metadata_json
) VALUES
('alice', 'alice@example.com', 29, 65000.50, TRUE, 4.5, '2026-03-10 09:15:00.000000', '2026-03-10 09:15:00', '1996-04-12', 'Team lead', JSON_OBJECT('dept','Engineering','city','Austin')),
('bob', 'bob@example.com', 34, 72000.00, TRUE, 4.2, '2026-03-17 14:55:01.600154', '2026-03-11 10:30:00', '1991-08-22', 'Backend dev', JSON_OBJECT('dept','Engineering','city','Seattle')),
('carol', 'carol@example.com', 41, 81000.75, FALSE, 3.9, '2026-03-09 08:05:00.000000', '2026-03-09 08:05:00', '1985-01-17', 'On leave', JSON_OBJECT('dept','HR','city','Denver')),
('dave', 'dave@example.com', 26, 54000.00, TRUE, 4.8, '2026-03-12 13:20:00.000000', '2026-03-12 13:20:00', '2000-11-02', 'Junior analyst', JSON_OBJECT('dept','Finance','city','Boston')),
('erin', 'erin@example.com', 31, 69000.25, TRUE, 4.1, '2026-03-08 17:45:00.000000', '2026-03-08 17:45:00', '1994-06-30', 'QA specialist', JSON_OBJECT('dept','QA','city','Chicago')),
('frank', 'frank@example.com', 38, 90500.00, TRUE, 4.7, '2026-03-14 07:55:00.000000', '2026-03-14 07:55:00', '1987-09-14', 'Architect', JSON_OBJECT('dept','Engineering','city','San Jose')),
('grace', 'grace@example.com', 27, 58000.40, FALSE, 3.5, '2026-03-07 19:10:00.000000', '2026-03-07 19:10:00', '1998-12-05', 'Contractor', JSON_OBJECT('dept','Support','city','Phoenix')),
('henry', 'henry@example.com', 45, 99000.99, TRUE, 4.9, '2026-03-13 11:11:00.000000', '2026-03-13 11:11:00', '1980-03-03', 'Principal engineer', JSON_OBJECT('dept','Engineering','city','New York')),
('ivy', 'ivy@example.com', 30, 67000.10, TRUE, 4.0, '2026-03-12 16:40:00.000000', '2026-03-12 16:40:00', '1995-07-19', 'Product manager', JSON_OBJECT('dept','Product','city','Portland')),
('jack', 'jack@example.com', 36, 76000.60, TRUE, 4.3, '2026-03-15 14:25:00.000000', '2026-03-15 14:25:00', '1989-02-28', 'DevOps', JSON_OBJECT('dept','Operations','city','Atlanta'));

-- Quick check
SELECT * FROM test_data_types;
```
## Example Payload

```json
{
    "id": 10,
    "username": "jack",
    "email": "jack@example.com",
    "age": 36,
    "salary": 76000.6,
    "is_active": true,
    "rating": 4.3,
    "created_at": "2026-03-15T14:25:00",
    "last_login_at": "2026-03-15T14:25:00",
    "birth_date": "1989-02-28",
    "notes": "DevOps",
    "metadata_json": "{\"city\": \"Atlanta\", \"dept\": \"Operations\"}"
  }
```



## Named Parameter Tests (One Column At A Time)

```sql
-- 1) id
SELECT * FROM test_data_types WHERE id = :ID;

-- 2) username
SELECT * FROM test_data_types WHERE username = :USERNAME;

-- 3) email
SELECT * FROM test_data_types WHERE email = :EMAIL;

-- 4) age
SELECT * FROM test_data_types WHERE age = :AGE;

-- 5) salary
SELECT * FROM test_data_types WHERE salary < :SALARY;

-- 6) is_active
SELECT * FROM test_data_types WHERE is_active = :ISACTIVE;

-- 7) rating (float-safe compare)
SELECT * FROM test_data_types WHERE ABS(rating - :RATING) < 0.0001;

-- 8) created_at
SELECT * FROM test_data_types WHERE created_at = :CREATEDAT;

-- 9) last_login_at
SELECT * FROM test_data_types WHERE last_login_at = :LASTLOGINAT;

-- 10) notes
SELECT * FROM test_data_types WHERE notes = :NOTES;

```

## Positional Parameter Tests (One Column At A Time)

```sql
-- 1) id
SELECT * FROM test_data_types WHERE id = ?;

-- 2) username
SELECT * FROM test_data_types WHERE username = ?;

-- 3) email
SELECT * FROM test_data_types WHERE email = ?;

-- 4) age
SELECT * FROM test_data_types WHERE age = ?;

-- 5) salary
SELECT * FROM test_data_types WHERE salary < ?;

-- 6) is_active
SELECT * FROM test_data_types WHERE is_active = ?;

-- 7) rating (float-safe compare)
SELECT * FROM test_data_types WHERE ABS(rating - ?) < 0.0001;

-- 8) created_at
SELECT * FROM test_data_types WHERE created_at = ?;

-- 9) notes
SELECT * FROM test_data_types WHERE notes = ?;

```

