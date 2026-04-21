package source.models;

import java.util.ArrayList;
import java.util.List;

public class TeachingAssistant extends Student {

    private List<Course> assistantCourses;

    public TeachingAssistant(String email, String password, String name) {
        super(email, password, name);
        this.role = "TA";
        this.assistantCourses = new ArrayList<>();
    }

    public List<Course> getAssistantCourses() {
        return assistantCourses;
    }

    public void assignAsAssistant(Course course) {
        if (!assistantCourses.contains(course)) {
            assistantCourses.add(course);
        }
    }

    @Override
    public void displayMenu() {
        System.out.println("\n+------- TEACHING ASSISTANT MENU -------+");
        System.out.println("| 1. View Available Courses             |");
        System.out.println("| 2. Register for Courses               |");
        System.out.println("| 3. View Schedule                      |");
        System.out.println("| 4. Track Academic Progress            |");
        System.out.println("| 5. Drop Course                        |");
        System.out.println("| 6. Submit Complaint                   |");
        System.out.println("| 7. View Complaint Status              |");
        System.out.println("| 8. Give Feedback                      |");
        System.out.println("| 9. View Enrolled Students (TA)        |");
        System.out.println("| 10. Assign Grades (TA)                |");
        System.out.println("| 11. Logout                            |");
        System.out.println("+---------------------------------------+");
        System.out.print("Choose option: ");
    }
}
