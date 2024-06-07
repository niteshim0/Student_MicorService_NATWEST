package com.example.demo.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.StudentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/students")
@Api(tags = "Student Management")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/upload")
    @ApiOperation(value = "Upload and process a CSV file", notes = "Uploads a CSV file and processes student eligibility")
    public ResponseEntity<byte[]> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        studentService.processCSV(file, outputStream);
        byte[] bytes = outputStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "processed_students.csv");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @PostMapping("/criteria")
    @ApiOperation(value = "Update eligibility criteria", notes = "Updates the eligibility criteria for student selection")
    public ResponseEntity<Void> updateCriteria(@RequestParam int science, @RequestParam int maths,
                                               @RequestParam int english, @RequestParam int computer) {
        studentService.updateCriteria(science, maths, english, computer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/eligibility/{rollNumber}")
    @ApiOperation(value = "Check eligibility", notes = "Checks the eligibility status of a student by roll number")
    public ResponseEntity<String> getEligibility(@PathVariable int rollNumber) {
        String eligibility = studentService.getEligibility(rollNumber);
        return new ResponseEntity<>(eligibility, HttpStatus.OK);
    }
}
