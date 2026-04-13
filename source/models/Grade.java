package source.models;

import java.io.Serializable;

public class Grade implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Student student;
    private Course course;
    private String letterGrade;
    private double gradePoints;

    public Grade(Student student, Course course, String letterGrade) {
        this.student = student;
        this.course = course;
        this.letterGrade = letterGrade;
        this.gradePoints = calculateGradePoints(letterGrade);
    }

    private double calculateGradePoints(String grade) {

        switch(grade.toUpperCase()) {

            case "A+": return 10.0;
            case "A" : return 10.0;
            case "A-": return 9.0;
            case "B" : return 8.0;
            case "B-": return 7.0;
            case "C" : return 6.0;
            case "C-": return 5.0;
            case "D" : return 4.0;
            case "F" : return 0.0;
            default: return 0.0;
        }
    }

    public Student getStudent() { 
        return student; 
    }

    public Course getCourse() { 
        return course; 
    }

    public String getLetterGrade() { 
        return letterGrade; 
    }

    public double getGradePoints() { 
        return gradePoints; 
    }

}
