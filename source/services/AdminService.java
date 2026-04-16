package source.services;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import source.models.Complaint;
import source.models.Course;
import source.models.Professor;
import source.models.Student;
import source.models.User;

public class AdminService {
    
    private DataStorage dataStorage;
    private Scanner scanner;
    
    public AdminService() {

        this.dataStorage = DataStorage.getInstance();
        this.scanner = new Scanner(System.in);
    }
    
    public void manageCourseCatalog() {

        System.out.println("\n    MANAGE COURSE CATALOG ");
        System.out.println("1. View All Courses");
        System.out.println("2. Add New Course");
        System.out.println("3. Delete Course");
        System.out.print("Choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch(choice) {

            case 1:
                viewAllCourses();
                break;
            case 2:
                addNewCourse();
                break;
            case 3:
                deleteCourse();
                break;
        }
    }
    
    private void viewAllCourses() {

        System.out.println("\n    ALL COURSES ");

        for (Course course : dataStorage.getCourses()) {
            System.out.println("\n" + course);
        }
    }
    
    private void addNewCourse() {

        System.out.println("\n    ADD NEW COURSE ");
        
        System.out.print("Course Code: ");
        String code = scanner.nextLine();
        
        System.out.print("Course Title: ");
        String title = scanner.nextLine();
        
        System.out.print("Credits (2 or 4): ");
        int credits = scanner.nextInt();
        
        System.out.print("Semester: ");
        int semester = scanner.nextInt();
        scanner.nextLine();
        
        Course course = new Course(code, title, credits, semester);
        dataStorage.addCourse(course);
        
        System.out.println("Course added successfully.");
    }
    
    private void deleteCourse() {

        System.out.println("\n    DELETE COURSE ");
        
        if (dataStorage.getCourses().isEmpty()) {
            System.out.println("No courses available.");
            return;
        }
        
        for (int i = 0; i < dataStorage.getCourses().size(); i++) {
            System.out.println((i + 1) + ". " + dataStorage.getCourses().get(i).getCourseCode());
        }
        
        System.out.print("Select course to delete: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice > 0 && choice <= dataStorage.getCourses().size()) {

            Course course = dataStorage.getCourses().get(choice - 1);
            dataStorage.getCourses().remove(course);
            dataStorage.saveData();
            System.out.println("Course deleted successfully.");
        }
    }
    
    public void manageStudentRecords() {

        System.out.println("\n    MANAGE STUDENT RECORDS ");
        
        List<User> students = dataStorage.getUsers().stream()
            .filter(u -> u instanceof Student)
            .collect(Collectors.toList());
        
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        for (int i = 0; i < students.size(); i++) {

            Student s = (Student) students.get(i);
            System.out.println((i + 1) + ". " + s.getName() + " (" + s.getEmail() + ")");
        }
        
        System.out.print("Select student to update: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice > 0 && choice <= students.size()) {

            Student student = (Student) students.get(choice - 1);
            
            System.out.println("\nCurrent Info:");
            System.out.println("Name: " + student.getName());
            System.out.println("Email: " + student.getEmail());
            System.out.println("Semester: " + student.getCurrentSemester());
            
            System.out.println("\nWhat would you like to update?");
            System.out.println("1. Name");
            System.out.println("2. Semester");
            System.out.print("Choice: ");
            
            int updateChoice = scanner.nextInt();
            scanner.nextLine();
            
            switch(updateChoice) {

                case 1:
                    System.out.print("Enter new name: ");
                    student.setName(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Enter new semester: ");
                    student.setCurrentSemester(scanner.nextInt());
                    break;
            }
            
            dataStorage.saveData();
            System.out.println("Student record updated.");
        }
    }
    
    public void assignProfessorsToCourses() {

        System.out.println("\n    ASSIGN PROFESSORS TO COURSES ");
        
        List<User> professors = dataStorage.getUsers().stream()
            .filter(u -> u instanceof Professor)
            .collect(Collectors.toList());
        
        if (professors.isEmpty() || dataStorage.getCourses().isEmpty()) {
            System.out.println("No professors or courses available.");
            return;
        }
        
        System.out.println("Select course:");
        for (int i = 0; i < dataStorage.getCourses().size(); i++) {

            Course c = dataStorage.getCourses().get(i);
            System.out.println((i + 1) + ". " + c.getCourseCode() + " - " + c.getTitle());
        }
        
        System.out.print("Choice: ");
        int courseChoice = scanner.nextInt();
        scanner.nextLine();
        
        if (courseChoice > 0 && courseChoice <= dataStorage.getCourses().size()) {

            Course course = dataStorage.getCourses().get(courseChoice - 1);
            
            System.out.println("\nSelect professor:");

            for (int i = 0; i < professors.size(); i++) {

                Professor p = (Professor) professors.get(i);
                System.out.println((i + 1) + ". " + p.getName() + " (" + p.getDepartment() + ")");
            }
            
            System.out.print("Choice: ");
            int profChoice = scanner.nextInt();
            scanner.nextLine();
            
            if (profChoice > 0 && profChoice <= professors.size()) {

                Professor professor = (Professor) professors.get(profChoice - 1);
                course.setProfessor(professor);
                professor.assignCourse(course);
                dataStorage.saveData();
                System.out.println("Professor assigned successfully.");
            }
        }
    }
    
    public void handleComplaints() {

        System.out.println("\n    HANDLE COMPLAINTS ");
        
        if (dataStorage.getComplaints().isEmpty()) {
            System.out.println("No complaints found.");
            return;
        }
        
        System.out.println("Filter by:");
        System.out.println("1. All Complaints");
        System.out.println("2. Pending Only");
        System.out.println("3. Resolved Only");
        System.out.print("Choice: ");
        
        int filterChoice = scanner.nextInt();
        scanner.nextLine();
        
        List<Complaint> filteredComplaints = dataStorage.getComplaints();
        
        if (filterChoice == 2) {

            filteredComplaints = filteredComplaints.stream()
                .filter(c -> c.getStatus().equals("Pending"))
                .collect(Collectors.toList());
        } 
        else if (filterChoice == 3) {

            filteredComplaints = filteredComplaints.stream()
                .filter(c -> c.getStatus().equals("Resolved"))
                .collect(Collectors.toList());
        }
        
        for (int i = 0; i < filteredComplaints.size(); i++) {
            System.out.println("\n" + (i + 1) + ". " + filteredComplaints.get(i));
        }
        
        System.out.print("\nSelect complaint to update (0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice > 0 && choice <= filteredComplaints.size()) {

            Complaint complaint = filteredComplaints.get(choice - 1);
            
            System.out.println("1. Mark as Resolved");
            System.out.println("2. Keep Pending");
            System.out.print("Choice: ");
            
            int statusChoice = scanner.nextInt();
            scanner.nextLine();
            
            if (statusChoice == 1) {
                
                System.out.print("Enter resolution details: ");
                String resolution = scanner.nextLine();
                
                complaint.setStatus("Resolved");
                complaint.setResolution(resolution);
                dataStorage.saveData();
                System.out.println("Complaint resolved.");
            }
        }
    }
}
