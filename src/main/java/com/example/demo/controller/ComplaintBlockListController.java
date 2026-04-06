package com.example.demo.controller;

import com.example.demo.model.ComplaintBlockList;
import com.example.demo.service.ComplaintBlockListService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/complaint-block-list")
@CrossOrigin(origins = "*")
public class ComplaintBlockListController {

    private final ComplaintBlockListService complaintBlockListService;

    public ComplaintBlockListController(ComplaintBlockListService complaintBlockListService) {
        this.complaintBlockListService = complaintBlockListService;
    }

    @GetMapping
    public ComplaintBlockList get() {
        return complaintBlockListService.get();
    }

    @PutMapping
    public ComplaintBlockList update(@RequestBody ComplaintBlockList complaintBlockList) {
        return complaintBlockListService.save(complaintBlockList);
    }
}
