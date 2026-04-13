package source.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Complaint implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private int id;
    private Student student;
    private String description;
    private String status; 
    private LocalDateTime date;
    private String resolution;
    private static int idCounter = 1;

    public Complaint(Student student, String description) {
        this.id = idCounter++;
        this.student = student;
        this.description = description;
        this.status = "Pending";
        this.date = LocalDateTime.now();
        this.resolution = "";
    }

    public int getId() { 
        return id; 
    }

    public Student getStudent() { 
        return student; 
    }

    public String getDescription() { 
        return description; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    public LocalDateTime getDate() { 
        return date; 
    }

    public String getResolution() { 
        return resolution; 
    }
    public void setResolution(String resolution) { 
        this.resolution = resolution; 
    }

    @Override
    public String toString() {
        
        return String.format("ID: %d | Student: %s | Status: %s | Date: %s\nDescription: %s%s",
            id, student.getName(), status,
            date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            description,
            !resolution.isEmpty() ? "\nResolution: " + resolution : "");
    }

}
