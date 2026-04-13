package source.models;

import java.util.ArrayList;
import java.util.List;

public class Professor extends User{
    
    private String department;
    private List<Course> assignedCourses;
    private String officeHours;

    public Professor(String email, String password, String name, String department) {
        super(email, password, name, "PROFESSOR");
        this.department = department;
        this.assignedCourses = new ArrayList<>();
        this.officeHours = "Mon-Fri 10:00-12:00";
    }

    public String getDepartment() { 
        return department; 
    }

    public void setDepartment(String department) { 
        this.department = department; 
    }

    public List<Course> getAssignedCourses() { 
        return assignedCourses; 
    }

    public String getOfficeHours() { 
        return officeHours; 
    }

    public void setOfficeHours(String officeHours) { 
        this.officeHours = officeHours; 
    }

    public void assignCourse(Course course) {
        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
        }
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== PROFESSOR MENU ===");
        System.out.println("1. View Assigned Courses");
        System.out.println("2. Update Course Details");
        System.out.println("3. View Enrolled Students");
        System.out.println("4. Assign Grades");
        System.out.println("5. Update Office Hours");
        System.out.println("6. Logout");
        System.out.print("Choose option: ");
    }
}
