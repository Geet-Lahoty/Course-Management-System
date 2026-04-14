package source.interfaces;

import java.util.List;

import source.models.Course;
import source.models.Professor;
import source.models.Student;

public interface CourseManagable {
    
    void addCourse(Course course);
    void removeCourse(String courseCode);
    void updateCourse(Course course);
    Course findCourseByCode(String courseCode);
    List<Course> getAllCourses();
    List<Course> getCoursesBySemester(int semester);
    void assignProfessorToCourse(String courseCode, Professor professor);
    void enrollStudentInCourse(String courseCode, Student student);
    void dropStudentFromCourse(String courseCode, Student student);
    List<Student> getEnrolledStudents(String courseCode);
    boolean validatePrerequisites(Course course, Student student);
    boolean checkCreditLimit(Student student, int additionalCredits);
}
