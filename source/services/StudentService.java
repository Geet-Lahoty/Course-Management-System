package source.services;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import source.models.Complaint;
import source.models.Course;
import source.models.Student;
import source.utils.GPAcalculator;

public class StudentService {
    
    private DataStorage dataStorage;
    private Scanner scanner;
    
    public StudentService() {
        this.dataStorage = DataStorage.getInstance();
        this.scanner = new Scanner(System.in);
    }
    
    public void viewAvailableCourses(Student student) {
        System.out.println("\n   AVAILABLE COURSES FOR SEMESTER " + student.getCurrentSemester());
        
        List<Course> availableCourses = dataStorage.getCourses().stream()
            .filter(c -> c.getSemester() == student.getCurrentSemester())
            .collect(Collectors.toList());
        
        if (availableCourses.isEmpty()) {
            System.out.println("No courses available for your current semester.");
            return;
        }
        
        for (Course course : availableCourses) {
            
            System.out.println("\n" + course);
            System.out.println("Timings: " + course.getTimings());
            System.out.println("Location: " + course.getLocation());
            System.out.println("Prerequisites: " + 
                (course.getPrerequisites().isEmpty() ? "None" : String.join(", ", course.getPrerequisites())));
            System.out.println("Enrolled: " + course.getEnrolledStudents().size() + "/" + course.getEnrollmentLimit());
        }
    }
    
    public void registerForCourse(Student student) {
        System.out.println("\n    REGISTER FOR COURSES");
        
        List<Course> availableCourses = dataStorage.getCourses().stream()
            .filter(c -> c.getSemester() == student.getCurrentSemester())
            .filter(c -> !student.getRegisteredCourses().contains(c))
            .collect(Collectors.toList());
        
        if (availableCourses.isEmpty()) {
            System.out.println("No courses available for registration.");
            return;
        }
        
        System.out.println("Available courses:");
        for (int i = 0; i < availableCourses.size(); i++) {
            System.out.println((i + 1) + ". " + availableCourses.get(i).getCourseCode() + 
                " - " + availableCourses.get(i).getTitle() + " (" + availableCourses.get(i).getCredits() + " credits)");
        }
        
        System.out.print("\nSelect course number (0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice > 0 && choice <= availableCourses.size()) {
            Course selectedCourse = availableCourses.get(choice - 1);
            
            // Check prerequisites
            if (!student.hasCompletedPrerequisites(selectedCourse)) {
                System.out.println("Cannot register. Prerequisites not met.");
                return;
            }
            
            // Check enrollment limit
            if (selectedCourse.getEnrolledStudents().size() >= selectedCourse.getEnrollmentLimit()) {
                System.out.println("Cannot register. Course is full.");
                return;
            }
            
            student.registerCourse(selectedCourse);
            selectedCourse.enrollStudent(student);
            dataStorage.saveData();
            System.out.println("Successfully registered for " + selectedCourse.getTitle());
        }
    }
    
    public void viewSchedule(Student student) {
        System.out.println("\n    WEEKLY SCHEDULE");
        
        if (student.getRegisteredCourses().isEmpty()) {
            System.out.println("No registered courses.");
            return;
        }
        
        for (Course course : student.getRegisteredCourses()) {
            System.out.println("\n" + course.getCourseCode() + ": " + course.getTitle());
            System.out.println("Timings: " + course.getTimings());
            System.out.println("Location: " + course.getLocation());
            System.out.println("Professor: " + 
                (course.getProfessor() != null ? course.getProfessor().getName() : "Not Assigned"));
        }
    }
    
    public void trackAcademicProgress(Student student) {
        System.out.println("\n    ACADEMIC PROGRESS ");
        
        if (student.getCompletedCourses().isEmpty()) {
            System.out.println("No completed courses yet.");
            return;
        }
        
        System.out.println("Completed Courses:");
        for (Map.Entry<Course, String> entry : student.getCompletedCourses().entrySet()) {
            System.out.println(entry.getKey().getCourseCode() + ": " + entry.getValue());
        }
        
        double sgpa = GPAcalculator.calculateSGPA(student, dataStorage.getGrades());
        double cgpa = GPAcalculator.calculateCGPA(student, dataStorage.getGrades());
        
        System.out.printf("\nSGPA: %.2f\n", sgpa);
        System.out.printf("CGPA: %.2f\n", cgpa);
    }
    
    public void dropCourse(Student student) {
        System.out.println("\n    DROP COURSE ");
        
        if (student.getRegisteredCourses().isEmpty()) {
            System.out.println("No registered courses to drop.");
            return;
        }
        
        System.out.println("Registered courses:");
        for (int i = 0; i < student.getRegisteredCourses().size(); i++) {
            Course c = student.getRegisteredCourses().get(i);
            System.out.println((i + 1) + ". " + c.getCourseCode() + " - " + c.getTitle());
        }
        
        System.out.print("\nSelect course number to drop (0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice > 0 && choice <= student.getRegisteredCourses().size()) {
            Course course = student.getRegisteredCourses().get(choice - 1);
            student.dropCourse(course);
            course.removeStudent(student);
            dataStorage.saveData();
            System.out.println("Successfully dropped " + course.getTitle());
        }
    }
    
    public void submitComplaint(Student student) {
        System.out.println("\n    SUBMIT COMPLAINT ");
        System.out.print("Enter complaint description: ");
        String description = scanner.nextLine();
        
        Complaint complaint = new Complaint(student, description);
        student.addComplaint(complaint);
        dataStorage.addComplaint(complaint);
        
        System.out.println("Complaint submitted successfully. ID: " + complaint.getId());
    }
    
    public void viewComplaintStatus(Student student) {
        System.out.println("\n    COMPLAINT STATUS ");
        
        if (student.getComplaints().isEmpty()) {
            System.out.println("No complaints submitted.");
            return;
        }
        
        for (Complaint complaint : student.getComplaints()) {
            System.out.println("\n" + complaint);
        }
    }
}
