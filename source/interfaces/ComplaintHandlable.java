package source.interfaces;

import java.util.List;

import source.models.Complaint;
import source.models.Student;

public interface ComplaintHandlable {

    void submitComplaint(Student student, String description);
    void updateComplaintStatus(int complaintId, String status, String resolution);
    List<Complaint> getAllComplaints();
    List<Complaint> getComplaintsByStudent(Student student);
    List<Complaint> getComplaintsByStatus(String status);
    Complaint findComplaintById(int id);
    void filterComplaintsByDate(String startDate, String endDate);
} 
