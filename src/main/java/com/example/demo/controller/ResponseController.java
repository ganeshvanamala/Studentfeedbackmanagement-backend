package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Response;
import com.example.demo.service.ResponseService;

import java.util.List;

@RestController
@RequestMapping("/api/responses")
@CrossOrigin("*")
public class ResponseController {

    @Autowired
    private ResponseService responseService;

    // SUBMIT RESPONSE
    @PostMapping
    public Response submit(@RequestBody Response response) {
        return responseService.submitResponse(response);
    }

    // GET RESPONSES BY FORM
    @GetMapping("/{formId}")
    public List<Response> getByForm(@PathVariable Long formId) {
        return responseService.getResponsesByForm(formId);
    }
}