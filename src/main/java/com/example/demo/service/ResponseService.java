package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Response;
import com.example.demo.repository.ResponseRepository;

import java.util.List;

@Service
public class ResponseService {

    @Autowired
    private ResponseRepository responseRepository;

    public Response submitResponse(Response response) {
        return responseRepository.save(response);
    }

    public List<Response> getResponsesByForm(Long formId) {
        return responseRepository.findByFormId(formId);
    }
}