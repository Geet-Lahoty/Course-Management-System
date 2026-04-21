# Course Management System

A comprehensive, command-line based Course Management System built with Java. This application provides a structured platform for administrators, professors, and students to manage academic activities within an educational institution. The system features role-based access control, ensuring that users only have access to functionalities relevant to their position.

## Features

The system is designed with three distinct user roles, each with a specific set of capabilities.

### Administrator
- **Course Management:** Add new courses to the catalog, remove existing courses, and view a comprehensive list of all available courses.
- **Student Record Management:** View and update student information, such as their name and current semester.
- **Faculty Assignment:** Assign professors to specific courses.
- **Complaint Resolution:** View, filter (by status), and resolve complaints submitted by students by providing resolution details.

### Professor
- **Course Administration:** View assigned courses and update details such as the syllabus, class timings, location, credits, prerequisites, and enrollment limits.
- **Class Roster:** View a list of all students enrolled in their assigned courses.
- **Grading:** Assign final letter grades (A+, A, B, etc.) to students for completed courses.
- **Office Hours:** Update and manage their office hours.

### Student
- **Course Registration:** View available courses for their semester, register for new courses, and drop existing ones.
- **Academic Planning:** View their weekly class schedule, including course timings, locations, and professors.
- **Progress Tracking:** Monitor academic performance by viewing completed courses and their calculated SGPA and CGPA.
- **Complaint System:** Submit complaints and view the status and resolution of their submissions.

## Project Structure

The repository is organized to separate different concerns of the application, promoting maintainability and scalability.

```
└── geet-lahoty-course-management-system/
    ├── data/               # Contains serialized .dat files for data persistence
    └── source/
        ├── interfaces/     # Defines contracts for core functionalities
        ├── main/           # Contains the main entry point of the application
        ├── models/         # Defines the core data objects (User, Course, Grade, etc.)
        ├── services/       # Implements the business logic for all features
        └── utils/          # Contains helper classes like the GPA Calculator
```

- **`data/`**: Stores the application's state (users, courses, grades, complaints) in serialized object files. This directory is created automatically if it doesn't exist.
- **`source/interfaces/`**: Contains Java interfaces (`CourseManagable`, `Authenticable`) that define the system's key operations.
- **`source/main/`**: The `Main.java` class serves as the application's entry point.
- **`source/models/`**: Includes classes that represent the core entities of the system, such as `Student`, `Professor`, `Administrator`, `Course`, and `Grade`.
- **`source/services/`**: Holds the service classes (`AdminService`, `StudentService`, etc.) that orchestrate the application's business logic and interact with the data layer.
- **`source/utils/`**: Provides utility classes for specific tasks, like `GPAcalculator` for academic calculations.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher.

### Installation & Compilation
1. Clone the repository to your local machine:
   ```sh
   git clone https://github.com/geet-lahoty/course-management-system.git
   ```
2. Navigate to the root directory of the project:
   ```sh
   cd course-management-system
   ```
3. Compile the Java source files:
   ```sh
   javac -d . source/**/*.java
   ```
   This command compiles all `.java` files and places the resulting `.class` files in the correct package structure within the root directory.

### Running the Application
The primary application logic (login, menu handling) is not yet implemented in the `source/main/Main.java` file. To run the system, you would need to implement the main application loop in this file to instantiate and utilize the service classes.

## Usage

Once the application is running, it operates as a menu-driven command-line interface. Users are first prompted to log in. Based on their role, a specific menu is displayed with the available options.

### Default Login Credentials
The system is pre-populated with default users for testing purposes.

- **Administrator:**
  - **Email:** `admin@svnit.ac.in`
  - **Password:** `admin123`

- **Student:**
  - **Email:** `u25ai069@aid.svnit.ac.in`
  - **Password:** `geet123`

- **Professor:**
  - **Email:** `Praveen@svnit.ac.in`
  - **Password:** `Praveen123`

## Data Persistence
The application uses Java's built-in serialization mechanism to persist data. All user, course, grade, and complaint objects are saved to `.dat` files within the `data/` directory. This ensures that the system's state is preserved between sessions. Data is automatically loaded when the application starts and saved whenever changes are made.
