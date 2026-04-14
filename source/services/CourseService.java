package source.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import source.interfaces.CourseManagable;
import source.models.Course;
import source.models.Professor;
import source.models.Student;

public class CourseService implements CourseManagable {
    
    private DataStorage dataStorage;
    private static CourseService instance;
    
    private CourseService() {
        this.dataStorage = DataStorage.getInstance();
    }
    
    public static CourseService getInstance() {
        if (instance == null) {
            instance = new CourseService();
        }
        return instance;
    }
    
    @Override
    public void addCourse(Course course) {
        if (findCourseByCode(course.getCourseCode()) == null) {
            dataStorage.getCourses().add(course);
            dataStorage.saveData();
            System.out.println("Course " + course.getCourseCode() + " added successfully.");
        } else {
            System.out.println("Course with code " + course.getCourseCode() + " already exists.");
        }
    }
    
    @Override
    public void removeCourse(String courseCode) {
        Course course = findCourseByCode(courseCode);
        if (course != null) {
            dataStorage.getCourses().remove(course);
            dataStorage.saveData();
            System.out.println("Course " + courseCode + " removed successfully.");
        } else {
            System.out.println("Course " + courseCode + " not found.");
        }
    }
    
    @Override
    public void updateCourse(Course course) {
        Course existingCourse = findCourseByCode(course.getCourseCode());
        if (existingCourse != null) {
            int index = dataStorage.getCourses().indexOf(existingCourse);
            dataStorage.getCourses().set(index, course);
            dataStorage.saveData();
            System.out.println("Course updated successfully.");
        }
    }
    
    @Override
    public Course findCourseByCode(String courseCode) {
        return dataStorage.getCourses().stream()
            .filter(c -> c.getCourseCode().equalsIgnoreCase(courseCode))
            .findFirst()
            .orElse(null);
    }
    
    @Override
    public List<Course> getAllCourses() {
        return new ArrayList<>(dataStorage.getCourses());
    }
    
    @Override
    public List<Course> getCoursesBySemester(int semester) {
        return dataStorage.getCourses().stream()
            .filter(c -> c.getSemester() == semester)
            .collect(Collectors.toList());
    }
    
    @Override
    public void assignProfessorToCourse(String courseCode, Professor professor) {
        Course course = findCourseByCode(courseCode);
        if (course != null) {
            course.setProfessor(professor);
            if (!professor.getAssignedCourses().contains(course)) {
                professor.assignCourse(course);
            }
            dataStorage.saveData();
            System.out.println("Professor " + professor.getName() + 
                " assigned to course " + courseCode);
        } else {
            System.out.println("Course not found.");
        }
    }
    
    @Override
    public void enrollStudentInCourse(String courseCode, Student student) {
        Course course = findCourseByCode(courseCode);
        if (course != null) {
            if (!validatePrerequisites(course, student)) {
                System.out.println("Prerequisites not met.");
                return;
            }
            
            if (!checkCreditLimit(student, course.getCredits())) {
                System.out.println("Credit limit exceeded.");
                return;
            }
            
            if (course.getEnrolledStudents().size() >= course.getEnrollmentLimit()) {
                System.out.println("Course is full.");
                return;
            }
            
            if (!course.getEnrolledStudents().contains(student)) {
                course.enrollStudent(student);
                student.registerCourse(course);
                dataStorage.saveData();
                System.out.println("Student enrolled successfully.");
            } else {
                System.out.println("Student already enrolled in this course.");
            }
        }
    }
    
    @Override
    public void dropStudentFromCourse(String courseCode, Student student) {
        Course course = findCourseByCode(courseCode);
        if (course != null) {
            course.removeStudent(student);
            student.dropCourse(course);
            dataStorage.saveData();
            System.out.println("Student dropped from course successfully.");
        }
    }
    
    @Override
    public List<Student> getEnrolledStudents(String courseCode) {
        Course course = findCourseByCode(courseCode);
        return course != null ? new ArrayList<>(course.getEnrolledStudents()) : new ArrayList<>();
    }
    
    @Override
    public boolean validatePrerequisites(Course course, Student student) {
        if (course.getPrerequisites().isEmpty()) {
            return true;
        }
        
        Set<String> completedCourseCodes = student.getCompletedCourses().keySet().stream()
            .map(Course::getCourseCode)
            .collect(Collectors.toSet());
        
        for (String prereq : course.getPrerequisites()) {
            if (!completedCourseCodes.contains(prereq)) {
                System.out.println("Missing prerequisite: " + prereq);
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean checkCreditLimit(Student student, int additionalCredits) {
        int currentCredits = student.getRegisteredCourses().stream()
            .mapToInt(Course::getCredits)
            .sum();
        return (currentCredits + additionalCredits) <= 20;
    }
    
    public List<Course> getCoursesByProfessor(Professor professor) {
        return dataStorage.getCourses().stream()
            .filter(c -> c.getProfessor() != null && 
                        c.getProfessor().getEmail().equals(professor.getEmail()))
            .collect(Collectors.toList());
    }
    
    public void displayCourseDetails(Course course) {
        System.out.println("\n=== COURSE DETAILS ===");
        System.out.println("Code: " + course.getCourseCode());
        System.out.println("Title: " + course.getTitle());
        System.out.println("Credits: " + course.getCredits());
        System.out.println("Semester: " + course.getSemester());
        System.out.println("Professor: " + 
            (course.getProfessor() != null ? course.getProfessor().getName() : "Not Assigned"));
        System.out.println("Timings: " + course.getTimings());
        System.out.println("Location: " + course.getLocation());
        System.out.println("Enrollment: " + course.getEnrolledStudents().size() + 
            "/" + course.getEnrollmentLimit());
        System.out.println("Prerequisites: " + 
            (course.getPrerequisites().isEmpty() ? "None" : 
             String.join(", ", course.getPrerequisites())));
        System.out.println("Syllabus: " + course.getSyllabus());
    }
    
    public boolean checkScheduleConflict(Student student, Course newCourse) {
        for (Course registeredCourse : student.getRegisteredCourses()) {
            if (hasTimeConflict(registeredCourse.getTimings(), newCourse.getTimings())) {
                System.out.println("Schedule conflict with " + registeredCourse.getCourseCode());
                return true;
            }
        }
        return false;
    }
    
    private boolean hasTimeConflict(String timings1, String timings2) {
        // Simplified time conflict check
        if (timings1.equals("TBA") || timings2.equals("TBA")) {
            return false;
        }
        
        String[] days1 = timings1.split(" ")[0].split("/");
        String[] days2 = timings2.split(" ")[0].split("/");
        
        for (String day1 : days1) {
            for (String day2 : days2) {
                if (day1.equals(day2)) {
                    // Same day - check time overlap
                    String time1 = timings1.split(" ")[1];
                    String time2 = timings2.split(" ")[1];
                    if (time1.equals(time2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
