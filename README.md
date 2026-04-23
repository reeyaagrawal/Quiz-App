# Quiz App

A Java Swing based desktop Quiz Application connected with MySQL database.

## Features

- User-friendly GUI using Java Swing
- Multiple quiz categories
- Timer for each question
- Previous and Next navigation
- Automatic score calculation
- Review answers after submission
- Retry quiz option
- Category selection screen
- MySQL database integration

## Technologies Used

- Java
- Swing
- JDBC
- MySQL
- Eclipse IDE

## Project Structure

src/
└── application/
- Field.java
- QuizApp.java
- Result.java
- ReviewAnswers.java

## Database

Database name: quiz_app

Each quiz category uses a separate table like:

- programming_quiz
- cn_quiz
- os_quiz
- basics_mcq
- ai_mcq
- gk_mcq

Table structure:

- id
- question
- option_a
- option_b
- option_c
- option_d
- answer

## Setup Instructions

### 1. Clone repository

git clone https://github.com/yourusername/Quiz-App.git

### 2. Add MySQL connector JAR

Add:

mysql-connector-j.jar
to project build path.

### 3. Create database

Create database in MySQL:

CREATE DATABASE quiz_app;


### 4. Update database password

Update your password in Java files:

```java
DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/quiz_app",
    "root",
    "your_password"
);
