package source.services;

import java.util.Scanner;

import source.models.Course;
import source.models.Grade;
import source.models.Professor;
import source.models.Student;

public class ProfessorService {

    private DataStorage dataStorage;
    private Scanner scanner;
    
    public ProfessorService() {
        this.dataStorage = DataStorage.getInstance();
        this.scanner = new Scanner(System.in);
    }
    
    public void viewAssignedCourses(Professor professor) {

        System.out.println("\n    ASSIGNED COURSES ");
        
        if (professor.getAssignedCourses().isEmpty()) {
            System.out.println("No courses assigned.");
            return;
        }
        
        for (Course course : professor.getAssignedCourses()) {

            System.out.println("\n" + course);
            System.out.println("Syllabus: " + course.getSyllabus());
            System.out.println("Office Hours: " + professor.getOfficeHours());
        }
    }
    
    public void updateCourseDetails(Professor professor) {

        System.out.println("\n    UPDATE COURSE DETAILS ");
        
        if (professor.getAssignedCourses().isEmpty()) {
            System.out.println("No courses assigned to update.");
            return;
        }
        
        System.out.println("Select course to update:");
        for (int i = 0; i < professor.getAssignedCourses().size(); i++) {

            System.out.println((i + 1) + ". " + professor.getAssignedCourses().get(i).getCourseCode() + 
                " - " + professor.getAssignedCourses().get(i).getTitle());
        }
        
        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice > 0 && choice <= professor.getAssignedCourses().size()) {

            Course course = professor.getAssignedCourses().get(choice - 1);
            
            System.out.println("\nWhat would you like to update?");
            System.out.println("1. Syllabus");
            System.out.println("2. Class Timings");
            System.out.println("3. Credits");
            System.out.println("4. Prerequisites");
            System.out.println("5. Enrollment Limit");
            System.out.println("6. Location");
            System.out.print("Choice: ");
            
            int updateChoice = scanner.nextInt();
            scanner.nextLine();
            
            switch(updateChoice) {

                case 1:
                    System.out.print("Enter new syllabus: ");
                    course.setSyllabus(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Enter new timings: ");
                    course.setTimings(scanner.nextLine());
                    break;
                case 3:
                    System.out.print("Enter credits (2 or 4): ");
                    int credits = scanner.nextInt();
                    if (credits == 2 || credits == 4) {
                        course.setCredits(credits);
                    }
                    break;
                case 4:
                    System.out.print("Enter prerequisite course code: ");
                    course.addPrerequisite(scanner.nextLine());
                    break;
                case 5:
                    System.out.print("Enter new enrollment limit: ");
                    course.setEnrollmentLimit(scanner.nextInt());
                    break;
                case 6:
                    System.out.print("Enter new location: ");
                    course.setLocation(scanner.nextLine());
                    break;
            }
            
            dataStorage.saveData();
            System.out.println("Course updated successfully.");
        }
    }
    
    public void viewEnrolledStudents(Professor professor) {

        System.out.println("\n    ENROLLED STUDENTS ");
        
        if (professor.getAssignedCourses().isEmpty()) {
            System.out.println("No courses assigned.");
            return;
        }
        
        System.out.println("Select course to view students:");
        for (int i = 0; i < professor.getAssignedCourses().size(); i++) {

            System.out.println((i + 1) + ". " + professor.getAssignedCourses().get(i).getCourseCode());
        }
        
        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice > 0 && choice <= professor.getAssignedCourses().size()) {

            Course course = professor.getAssignedCourses().get(choice - 1);
            
            if (course.getEnrolledStudents().isEmpty()) {
                System.out.println("No students enrolled in this course.");
                return;
            }
            
            System.out.println("\nStudents enrolled in " + course.getTitle() + ":");

            for (Student student : course.getEnrolledStudents()) {

                System.out.println("\nName: " + student.getName());
                System.out.println("Email: " + student.getEmail());
                System.out.println("Semester: " + student.getCurrentSemester());
            }
        }
    }
    
    public void assignGrades(Professor professor) {

        System.out.println("\n    ASSIGN GRADES ");
        
        if (professor.getAssignedCourses().isEmpty()) {

            System.out.println("No courses assigned.");
            return;
        }
        
        System.out.println("Select course to assign grades:");
        for (int i = 0; i < professor.getAssignedCourses().size(); i++) {

            System.out.println((i + 1) + ". " + professor.getAssignedCourses().get(i).getCourseCode());
        }
        
        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice > 0 && choice <= professor.getAssignedCourses().size()) {

            Course course = professor.getAssignedCourses().get(choice - 1);
            
            if (course.getEnrolledStudents().isEmpty()) {
                System.out.println("No students enrolled in this course.");
                return;
            }
            
            for (Student student : course.getEnrolledStudents()) {
                
                System.out.println("\nStudent: " + student.getName());
                System.out.print("Enter grade (A+, A, A-, B, B-, C, C-, D, F): ");
                String grade = scanner.nextLine();
                
                Grade gradeObj = new Grade(student, course, grade);
                dataStorage.addGrade(gradeObj);
                student.completeCourse(course, grade);
            }
            
            dataStorage.saveData();
            System.out.println("Grades assigned successfully.");
        }
    }
}