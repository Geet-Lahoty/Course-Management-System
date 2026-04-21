package source.models;

import java.io.Serializable;

public class Feedback<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private Student student;
    private Course course;
    private T feedbackData;

    public Feedback(Student student, Course course, T feedbackData) {
        this.student = student;
        this.course = course;
        this.feedbackData = feedbackData;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public T getFeedbackData() {
        return feedbackData;
    }

    @Override
    public String toString() {
        return "Student: " + student.getName() + " | Course: " + course.getTitle() + " | Feedback: " + feedbackData.toString();
    }
}
