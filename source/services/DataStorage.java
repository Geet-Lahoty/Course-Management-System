package source.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import source.models.Administrator;
import source.models.Complaint;
import source.models.Course;
import source.models.Feedback;
import source.models.Grade;
import source.models.Professor;
import source.models.Student;
import source.models.User;

public class DataStorage {
    
    private static DataStorage instance;
    private List<User> users;
    private List<Course> courses;
    private List<Complaint> complaints;
    private List<Grade> grades;
    private List<Feedback<?>> feedbacks;

    private static final String DATA_DIR = "data/";
    private static final String USERS_FILE = DATA_DIR + "users.dat";
    private static final String COURSES_FILE = DATA_DIR + "courses.dat";
    private static final String COMPLAINTS_FILE = DATA_DIR + "complaints.dat";
    private static final String GRADES_FILE = DATA_DIR + "grades.dat";
    private static final String FEEDBACKS_FILE = DATA_DIR + "feedbacks.dat";

    private DataStorage() {

        createDataDirectory();
        users = new ArrayList<>();
        courses = new ArrayList<>();
        complaints = new ArrayList<>();
        grades = new ArrayList<>();
        feedbacks = new ArrayList<>();
        loadData();
        
        if (users.isEmpty()) {
            initializeDefaultData();
        }
    }

    public static DataStorage getInstance() {

        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    private void createDataDirectory() {

        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private void initializeDefaultData() {
        users.add(new Administrator("admin@svnit.ac.in", "System Administrator"));
        
        users.add(new Professor("Praveen@svnit.ac.in", "Praveen123", "Dr. Praveen Kumar", "Artificial Intelligence"));
        users.add(new Professor("RahulShrivastav@svnit.ac.in", "Rahul1234", "Dr. Rahul Shrivastava", "Artificial Intelligence"));
        
        users.add(new Student("u25ai069@aid.svnit.ac.in", "geet123", "Geet Lahoty"));
        users.add(new Student("i25ai013@aid.svnit.ac.in", "kanishtha123", "Kanishtha Maheshwari"));
        users.add(new Student("u25ai048@aid.svnit.ac.in", "heshika123", "Heshika Pareek"));
        users.add(new Student("u25ai034@aid.svnit.ac.in", "harshil123", "Harshil Mistry"));
        
        courses.add(new Course("AI101", "Introduction to Computer Science", 4, 1));
        courses.add(new Course("AI103", "Introduction to Programming", 4, 1));
        courses.add(new Course("HS110", "English", 4, 1));
        courses.add(new Course("AI105", "Basics of Electrical and Electronics", 4, 1));
        courses.add(new Course("MA105", "Fundamentals of Maths", 4, 1));
        
        for (Course c : courses) {
            c.setTimings("Mon/Wed 10:00-11:30");
            c.setLocation("Room " + (100 + courses.indexOf(c)));
        }
        
        saveData();
    }

    @SuppressWarnings("unchecked")
    private void loadData() {

        try {

            if (new File(USERS_FILE).exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE));
                users = (List<User>) ois.readObject();
                ois.close();
            }
            
            if (new File(COURSES_FILE).exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(COURSES_FILE));
                courses = (List<Course>) ois.readObject();
                ois.close();
            }
            
            if (new File(COMPLAINTS_FILE).exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(COMPLAINTS_FILE));
                complaints = (List<Complaint>) ois.readObject();
                ois.close();
            }
            
            if (new File(GRADES_FILE).exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GRADES_FILE));
                grades = (List<Grade>) ois.readObject();
                ois.close();
            }

            if (new File(FEEDBACKS_FILE).exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FEEDBACKS_FILE));
                feedbacks = (List<Feedback<?>>) ois.readObject();
                ois.close();
            }
        } 
        catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    public void saveData() {

        try {

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE));
            oos.writeObject(users);
            oos.close();
            
            oos = new ObjectOutputStream(new FileOutputStream(COURSES_FILE));
            oos.writeObject(courses);
            oos.close();
            
            oos = new ObjectOutputStream(new FileOutputStream(COMPLAINTS_FILE));
            oos.writeObject(complaints);
            oos.close();
            
            oos = new ObjectOutputStream(new FileOutputStream(GRADES_FILE));
            oos.writeObject(grades);
            oos.close();

            oos = new ObjectOutputStream(new FileOutputStream(FEEDBACKS_FILE));
            oos.writeObject(feedbacks);
            oos.close();
        } 
        catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public List<User> getUsers() { 
        return users; 
    }

    public List<Course> getCourses() { 
        return courses; 
    }

    public List<Complaint> getComplaints() { 
        return complaints; 
    }

    public List<Grade> getGrades() { 
        return grades; 
    }

    public List<Feedback<?>> getFeedbacks() { 
        return feedbacks; 
    }
    
    public User findUserByEmail(String email) {
        return users.stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElse(null);
    }
    
    public void addUser(User user) {
        users.add(user);
        saveData();
    }
    
    public void addCourse(Course course) {
        courses.add(course);
        saveData();
    }
    
    public void addComplaint(Complaint complaint) {
        complaints.add(complaint);
        saveData();
    }
    
    public void addGrade(Grade grade) {
        grades.add(grade);
        saveData();
    }

    public void addFeedback(Feedback<?> feedback) {
        feedbacks.add(feedback);
        saveData();
    }

}
