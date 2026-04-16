package source.main;

import java.util.Scanner;

import source.models.Administrator;
import source.models.Professor;
import source.models.Student;
import source.models.User;
import source.services.AdminService;
import source.services.DataStorage;
import source.services.ProfessorService;
import source.services.StudentService;

public class Main {

    private static DataStorage dataStorage;
    private static StudentService studentService;
    private static ProfessorService professorService;
    private static AdminService adminService;
    private static Scanner scanner;
    private static User currentUser;
    
    public static void main(String[] args) {
        initialize();
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } 
            else {
                handleUserSession();
            }
        }
    }
    
    private static void initialize() {

        dataStorage = DataStorage.getInstance();
        studentService = new StudentService();
        professorService = new ProfessorService();
        adminService = new AdminService();
        scanner = new Scanner(System.in);
    }
    
    private static void showLoginMenu() {

        System.out.println("\n+-- UNIVERSITY COURSE REGISTRATION SYSTEM --+");
        System.out.println("| 1. Enter the Application                  |");
        System.out.println("| 2. Exit the Application                   |");
        System.out.println("+-------------------------------------------+");
        System.out.print("Choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice == 1) {
            login();
        } 
        else if (choice == 2) {
            System.out.println("Thank you for using the system. Goodbye!");
            System.exit(0);
        }
    }
    
    private static void login() {

        System.out.println("\n+----   LOGIN   ----+");
        System.out.println("| Login as:         |");
        System.out.println("| 1. Student        |");
        System.out.println("| 2. Professor      |");
        System.out.println("| 3. Administrator  |");
        System.out.println("+-------------------+");
        System.out.print("Choice: ");
        
        int roleChoice = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        String role = switch(roleChoice) {

            case 1 -> "STUDENT";
            case 2 -> "PROFESSOR";
            case 3 -> "ADMINISTRATOR";
            default -> "";
        };
        
        if (roleChoice == 1 || roleChoice == 2) {

            User user = dataStorage.findUserByEmail(email);
            
            if (user != null && user.getPassword().equals(password) && user.getRole().equals(role)) {
                currentUser = user;
                System.out.println("\nWelcome, " + currentUser.getName() + "!");
            } 
            else {
                System.out.println("\nInvalid credentials. Would you like to sign up? (y/n)");
                if (scanner.nextLine().equalsIgnoreCase("y")) {
                    signup(roleChoice);
                }
            }
        } 
        else if (roleChoice == 3) {

            User user = dataStorage.findUserByEmail(email);
            
            if (user instanceof Administrator && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("\nWelcome, Administrator " + currentUser.getName() + "!");
            } else {
                System.out.println("\nInvalid administrator credentials.");
            }
        }
    }
    
    private static void signup(int roleChoice) {

        System.out.println("\n    SIGN UP ");
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        if (dataStorage.findUserByEmail(email) != null) {
            System.out.println("Email already registered.");
            return;
        }
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        System.out.print("Name: ");
        String name = scanner.nextLine();
        
        User newUser = null;
        
        if (roleChoice == 1) {
            newUser = new Student(email, password, name);
        } 
        else if (roleChoice == 2) {
            System.out.print("Department: ");
            String dept = scanner.nextLine();
            newUser = new Professor(email, password, name, dept);
        }
        
        if (newUser != null) {
            dataStorage.addUser(newUser);
            currentUser = newUser;
            System.out.println("\nAccount created successfully! Welcome, " + name + "!");
        }
    }
    
    private static void handleUserSession() {

        currentUser.displayMenu();
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (currentUser instanceof Student) {
            handleStudentSession((Student) currentUser, choice);
        } 
        else if (currentUser instanceof Professor) {
            handleProfessorSession((Professor) currentUser, choice);
        } 
        else if (currentUser instanceof Administrator) {
            handleAdminSession((Administrator) currentUser, choice);
        }
    }
    
    private static void handleStudentSession(Student student, int choice) {

        switch(choice) {

            case 1 -> studentService.viewAvailableCourses(student);
            case 2 -> studentService.registerForCourse(student);
            case 3 -> studentService.viewSchedule(student);
            case 4 -> studentService.trackAcademicProgress(student);
            case 5 -> studentService.dropCourse(student);
            case 6 -> studentService.submitComplaint(student);
            case 7 -> studentService.viewComplaintStatus(student);
            case 8 -> {
                currentUser = null;
                dataStorage.saveData();
                System.out.println("Logged out successfully.");
            }
        }
    }
    
    private static void handleProfessorSession(Professor professor, int choice) {

        switch(choice) {

            case 1 -> professorService.viewAssignedCourses(professor);
            case 2 -> professorService.updateCourseDetails(professor);
            case 3 -> professorService.viewEnrolledStudents(professor);
            case 4 -> professorService.assignGrades(professor);
            case 5 -> {
                System.out.print("Enter new office hours: ");
                professor.setOfficeHours(scanner.nextLine());
                dataStorage.saveData();
                System.out.println("Office hours updated.");
            }
            case 6 -> {
                currentUser = null;
                dataStorage.saveData();
                System.out.println("Logged out successfully.");
            }
        }
    }
    
    private static void handleAdminSession(Administrator admin, int choice) {

        switch(choice) {

            case 1 -> adminService.manageCourseCatalog();
            case 2 -> adminService.manageStudentRecords();
            case 3 -> adminService.assignProfessorsToCourses();
            case 4 -> adminService.handleComplaints();
            case 5 -> viewAllStudents();
            case 6 -> viewAllProfessors();
            case 7 -> completeSemester();
            case 8 -> {
                currentUser = null;
                dataStorage.saveData();
                System.out.println("Logged out successfully.");
            }
        }
    }
    
    private static void viewAllStudents() {

        System.out.println("\n    ALL STUDENTS ");
        dataStorage.getUsers().stream()
            .filter(u -> u instanceof Student)
            .forEach(s -> System.out.println(s.getName() + " - " + s.getEmail()));
    }
    
    private static void viewAllProfessors() {

        System.out.println("\n    ALL PROFESSORS ");
        dataStorage.getUsers().stream()
            .filter(u -> u instanceof Professor)
            .forEach(p -> System.out.println(p.getName() + " - " + p.getEmail()));
    }
    
    private static void completeSemester() {

        System.out.println("\n    COMPLETE SEMESTER ");
        System.out.print("Are you sure all grades have been assigned? (y/n): ");
        
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            dataStorage.getUsers().stream()
                .filter(u -> u instanceof Student)
                .forEach(s -> {
                    Student student = (Student) s;
                    student.setCurrentSemester(student.getCurrentSemester() + 1);
                });
            
            dataStorage.saveData();
            System.out.println("Semester completed. All students advanced to next semester.");
        }
    }
}