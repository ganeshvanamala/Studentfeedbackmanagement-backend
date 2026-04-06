package com.example.demo.controller;

import com.example.demo.model.Complaint;
import com.example.demo.service.ComplaintService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping
    public List<Complaint> getAll() {
        return complaintService.findAll();
    }

    @PostMapping
    public Complaint create(@RequestBody Complaint complaint) {
        return complaintService.save(complaint);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        complaintService.deleteById(id);
    }

    @PutMapping("/{id}/plagged")
    public Complaint updatePlagged(@PathVariable Long id, @RequestParam boolean value) {
        return complaintService.updatePlagged(id, value);
    }
}
