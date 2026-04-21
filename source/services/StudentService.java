package source.services;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import source.models.Complaint;
import source.models.Course;
import source.models.TeachingAssistant;
import source.models.Feedback;
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
    
    public void registerForCourse(Student student) throws source.utils.CourseFullException {

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

            if (!student.hasCompletedPrerequisites(selectedCourse)) {
                System.out.println("Cannot register. Prerequisites not met.");
                return;
            }

            if (selectedCourse.getEnrolledStudents().size() >= selectedCourse.getEnrollmentLimit()) {
                throw new source.utils.CourseFullException("Cannot register. Course is full.");
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
    
    public void dropCourse(Student student) throws source.utils.DropDeadlinePassedException {

        System.out.println("\n    DROP COURSE ");
        
        System.out.print("Has the drop deadline passed? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            throw new source.utils.DropDeadlinePassedException("Deadline passed! You cannot drop courses anymore.");
        }
        
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

    public void giveFeedback(Student student) {
        System.out.println("\n    GIVE FEEDBACK ");
        if (student.getCompletedCourses().isEmpty()) {
            System.out.println("No completed courses to give feedback for.");
            return;
        }

        System.out.println("Completed courses:");
        List<Course> comps = new java.util.ArrayList<>(student.getCompletedCourses().keySet());
        for (int i = 0; i < comps.size(); i++) {
            System.out.println((i + 1) + ". " + comps.get(i).getCourseCode() + " - " + comps.get(i).getTitle());
        }

        System.out.print("Select course number: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= comps.size()) {
            Course course = comps.get(choice - 1);
            System.out.println("Select feedback type: 1. Numeric Rating (1-5)  2. Textual Comment");
            int type = scanner.nextInt();
            scanner.nextLine();
            
            if (type == 1) {
                System.out.print("Enter rating (1-5): ");
                Integer rating = scanner.nextInt();
                scanner.nextLine();
                Feedback<Integer> feedback = new Feedback<>(student, course, rating);
                dataStorage.addFeedback(feedback);
                System.out.println("Feedback submitted!");
            } else if (type == 2) {
                System.out.print("Enter comment: ");
                String comment = scanner.nextLine();
                Feedback<String> feedback = new Feedback<>(student, course, comment);
                dataStorage.addFeedback(feedback);
                System.out.println("Feedback submitted!");
            }
        }
    }

    public void viewEnrolledStudentsTA(TeachingAssistant ta) {
        System.out.println("\n    ENROLLED STUDENTS (TA) ");
        if (ta.getAssistantCourses().isEmpty()) {
            System.out.println("No courses assigned as TA.");
            return;
        }

        System.out.println("Select course:");
        for (int i = 0; i < ta.getAssistantCourses().size(); i++) {
            System.out.println((i + 1) + ". " + ta.getAssistantCourses().get(i).getCourseCode());
        }

        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= ta.getAssistantCourses().size()) {
            Course course = ta.getAssistantCourses().get(choice - 1);
            for (Student student : course.getEnrolledStudents()) {
                System.out.println("\nName: " + student.getName());
                System.out.println("Email: " + student.getEmail());
            }
        }
    }

    public void assignGradesTA(TeachingAssistant ta) {
        System.out.println("\n    ASSIGN GRADES (TA) ");
        if (ta.getAssistantCourses().isEmpty()) {
            System.out.println("No courses assigned as TA.");
            return;
        }

        System.out.println("Select course:");
        for (int i = 0; i < ta.getAssistantCourses().size(); i++) {
            System.out.println((i + 1) + ". " + ta.getAssistantCourses().get(i).getCourseCode());
        }

        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= ta.getAssistantCourses().size()) {
            Course course = ta.getAssistantCourses().get(choice - 1);
            for (Student student : course.getEnrolledStudents()) {
                System.out.println("\nStudent: " + student.getName());
                System.out.print("Enter grade: ");
                String grade = scanner.nextLine();
                source.models.Grade gradeObj = new source.models.Grade(student, course, grade);
                dataStorage.addGrade(gradeObj);
                student.completeCourse(course, grade);
            }
            dataStorage.saveData();
            System.out.println("Grades assigned by TA!");
        }
    }
}
