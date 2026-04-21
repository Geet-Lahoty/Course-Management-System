package source.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private String courseCode;
    private String title;
    private Professor professor;
    private int credits;
    private List<String> prerequisites;
    private String timings;
    private String location;
    private int enrollmentLimit;
    private List<Student> enrolledStudents;
    private String syllabus;
    private int semester;

    public Course(String courseCode, String title, int credits, int semester) {
        this.courseCode = courseCode;
        this.title = title;
        this.credits = credits;
        this.semester = semester;
        this.prerequisites = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.enrollmentLimit = 30;
        this.timings = "TBA";
        this.location = "TBA";
        this.syllabus = "Syllabus not set";
    }

    public String getCourseCode() { 
        return courseCode; 
    }

    public String getTitle() { 
        return title; 
    }

    public Professor getProfessor() { 
        return professor; 
    }
    public void setProfessor(Professor professor) { 
        this.professor = professor; 
    }

    public int getCredits() { 
        return credits; 
    }
    public void setCredits(int credits) { 
        this.credits = credits; 
    }

    public int getSemester() {
        return semester;
    }

    public List<String> getPrerequisites() { 
        return prerequisites; 
    }
    public void addPrerequisite(String prereq) { 
        prerequisites.add(prereq); 
    }

    public String getTimings() { 
        return timings; 
    }
    public void setTimings(String timings) { 
        this.timings = timings; 
    }

    public String getLocation() { 
        return location; 
    }
    public void setLocation(String location) { 
        this.location = location; 
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public int getEnrollmentLimit() { 
        return enrollmentLimit; 
    }
    public void setEnrollmentLimit(int limit) { 
        this.enrollmentLimit = limit; 
    }

    public String getSyllabus() { 
        return syllabus; 
    }
    public void setSyllabus(String syllabus) { 
        this.syllabus = syllabus; 
    }

    public void enrollStudent(Student student) throws source.utils.CourseFullException {
        if (enrolledStudents.size() < enrollmentLimit) {
            enrolledStudents.add(student);
        } else {
            throw new source.utils.CourseFullException("Cannot enroll: Course " + courseCode + " is already full.");
        }
    }

    public void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }

    @Override
    public String toString() {
        return String.format("%s: %s (Credits: %d, Semester: %d, Professor: %s)",
            courseCode, title, credits, semester,
            professor != null ? professor.getName() : "Not Assigned");
    }

}
