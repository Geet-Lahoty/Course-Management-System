package source.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User{

    private int currentSemester;
    private List<Course> registeredCourses; 
    private Map<Course, String> completedCourses; 
    private List<Complaint> complaints;

    public Student(String email, String password, String name) {
        super(email, password, name, "STUDENT");
        this.currentSemester = 1;
        this.registeredCourses = new ArrayList<>();
        this.completedCourses = new HashMap<>();
        this.complaints = new ArrayList<>();
    }
    
    public int getCurrentSemester() { 
        return currentSemester; 
    }
    public void setCurrentSemester(int semester) { 
        this.currentSemester = semester; 
    }


    public List<Course> getRegisteredCourses() { 
        return registeredCourses; 
    }

    public Map<Course, String> getCompletedCourses() { 
        return completedCourses; 
    }

    public List<Complaint> getComplaints() { 
        return complaints; 
    }

    public void registerCourse(Course course) {

        if (!registeredCourses.contains(course)) {
            registeredCourses.add(course);
        }
    }

    public void dropCourse(Course course) {
        registeredCourses.remove(course);
    }
    
    public void completeCourse(Course course, String grade) {
        completedCourses.put(course, grade);
    }

    public void addComplaint(Complaint complaint) {
        complaints.add(complaint);
    }

    public int getTotalCredits() {
        return registeredCourses.stream().mapToInt(Course::getCredits).sum();
    }
    
    public boolean hasCompletedPrerequisites(Course course) {

        for (String prereq : course.getPrerequisites()) {

            boolean completed = completedCourses.keySet().stream()
                .anyMatch(c -> c.getCourseCode().equals(prereq));
            if (!completed) return false;
        }
        return true;
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== STUDENT MENU ===");
        System.out.println("1. View Available Courses");
        System.out.println("2. Register for Courses");
        System.out.println("3. View Schedule");
        System.out.println("4. Track Academic Progress");
        System.out.println("5. Drop Course");
        System.out.println("6. Submit Complaint");
        System.out.println("7. View Complaint Status");
        System.out.println("8. Logout");
        System.out.print("Choose option: ");
    }
}
