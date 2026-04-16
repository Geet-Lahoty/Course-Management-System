package source.models;

public class Administrator extends User{
    
    private static final String ADMIN_PASSWORD = "admin123";
    
    public Administrator(String email, String name) {
        super(email, ADMIN_PASSWORD, name, "ADMINISTRATOR");
    }
    
    @Override
    public void displayMenu() {

        System.out.println("\n+------- ADMINISTRATOR MENU -------+");
        System.out.println("| 1. Manage Course Catalog         |");
        System.out.println("| 2. Manage Student Records        |");
        System.out.println("| 3. Assign Professors to Courses  |");
        System.out.println("| 4. Handle Complaints             |");
        System.out.println("| 5. View All Students             |");
        System.out.println("| 6. View All Professors           |");
        System.out.println("| 7. Complete Semester             |");
        System.out.println("| 8. Logout                        |");
        System.out.println("+----------------------------------+");
        System.out.print("Choose option: ");
    }
    
}
