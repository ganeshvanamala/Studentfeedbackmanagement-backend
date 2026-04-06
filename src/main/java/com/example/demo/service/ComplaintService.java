package com.example.demo.service;

import com.example.demo.model.Complaint;
import com.example.demo.repository.ComplaintRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    public Complaint save(Complaint complaint) {
        if (complaint.getPlagged() == null) {
            complaint.setPlagged(false);
        }
        return complaintRepository.save(complaint);
    }

    public List<Complaint> findAll() {
        return complaintRepository.findAllByOrderByIdDesc();
    }

    public void deleteById(Long id) {
        complaintRepository.deleteById(id);
    }

    public Complaint updatePlagged(Long id, boolean plagged) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaint.setPlagged(plagged);
        return complaintRepository.save(complaint);
    }
}
