package com.example.demo.service;

import com.example.demo.model.ComplaintBlockList;
import com.example.demo.repository.ComplaintBlockListRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ComplaintBlockListService {

    private static final long SINGLETON_ID = 1L;

    private final ComplaintBlockListRepository repository;

    public ComplaintBlockListService(ComplaintBlockListRepository repository) {
        this.repository = repository;
    }

    public ComplaintBlockList get() {
        return repository.findById(SINGLETON_ID).orElseGet(() -> {
            ComplaintBlockList created = new ComplaintBlockList();
            created.setId(SINGLETON_ID);
            created.setAcademics(new ArrayList<>());
            created.setSports(new ArrayList<>());
            created.setHostel(new ArrayList<>());
            created.setCategoryBlocked(new HashMap<>());
            return repository.save(created);
        });
    }

    public ComplaintBlockList save(ComplaintBlockList payload) {
        ComplaintBlockList existing = get();
        existing.setAcademics(payload.getAcademics());
        existing.setSports(payload.getSports());
        existing.setHostel(payload.getHostel());
        existing.setCategoryBlocked(payload.getCategoryBlocked());
        return repository.save(existing);
    }
}
