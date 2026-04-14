package source.utils;

import java.util.List;

import source.models.Grade;
import source.models.Student;

public class GPAcalculator {
    
    public static double calculateSGPA(Student student, List<Grade> allGrades) {
        
        List<Grade> studentGrades = allGrades.stream()
            .filter(g -> g.getStudent().getEmail().equals(student.getEmail()))
            .filter(g -> g.getCourse().getSemester() == student.getCurrentSemester() - 1)
            .toList();
        
        if (studentGrades.isEmpty()) return 0.0;
        
        double totalGradePoints = 0;
        int totalCredits = 0;
        
        for (Grade grade : studentGrades) {
            totalGradePoints += grade.getGradePoints() * grade.getCourse().getCredits();
            totalCredits += grade.getCourse().getCredits();
        }
        
        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }
    
    public static double calculateCGPA(Student student, List<Grade> allGrades) {
        List<Grade> studentGrades = allGrades.stream()
            .filter(g -> g.getStudent().getEmail().equals(student.getEmail()))
            .toList();
        
        if (studentGrades.isEmpty()) return 0.0;
        
        double totalGradePoints = 0;
        int totalCredits = 0;
        
        for (Grade grade : studentGrades) {
            totalGradePoints += grade.getGradePoints() * grade.getCourse().getCredits();
            totalCredits += grade.getCourse().getCredits();
        }
        
        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }
}
