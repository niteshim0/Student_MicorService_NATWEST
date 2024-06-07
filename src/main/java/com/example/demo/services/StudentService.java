package com.example.demo.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.beans.Student;
import com.example.demo.repositories.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private volatile int scienceCriteria = 85;
    private volatile int mathsCriteria = 90;
    private volatile int englishCriteria = 75;
    private volatile int computerCriteria = 95;

    public void updateCriteria(int science, int maths, int english, int computer) {
        this.scienceCriteria = science;
        this.mathsCriteria = maths;
        this.englishCriteria = english;
        this.computerCriteria = computer;
    }

    public List<Student> parseCSV(MultipartFile file) throws IOException {
        List<Student> students = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Student student = new Student();
                student.setRollNumber(Integer.parseInt(csvRecord.get("roll number")));
                student.setStudentName(csvRecord.get("student name"));
                student.setScienceMarks(Double.parseDouble(csvRecord.get("science")));
                student.setMathsMarks(Double.parseDouble(csvRecord.get("maths")));
                student.setEnglishMarks(Double.parseDouble(csvRecord.get("english")));
                student.setComputerMarks(Double.parseDouble(csvRecord.get("computer")));
                student.setEligibility("ToBeChecked");

                students.add(student);
            }
        }

        return students;
    }

    public void processCSV(MultipartFile file, OutputStream outputStream) throws IOException {
        List<Student> students = parseCSV(file);

        students.parallelStream().forEach(student -> {
            String eligibility = checkEligibility(student);
            student.setEligibility(eligibility);
            studentRepository.save(student);
        });

        try (CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), CSVFormat.DEFAULT.withHeader(
                "roll number", "student name", "science", "maths", "english", "computer", "Eligible"))) {
            for (Student student : students) {
                csvPrinter.printRecord(student.getRollNumber(), student.getStudentName(), student.getScienceMarks(),
                        student.getMathsMarks(), student.getEnglishMarks(), student.getComputerMarks(), student.getEligibility());
            }
        }
    }

    public String checkEligibility(Student student) {
        if (student.getScienceMarks() > scienceCriteria && student.getMathsMarks() > mathsCriteria &&
                student.getEnglishMarks() > englishCriteria && student.getComputerMarks() > computerCriteria) {
            return "YES";
        } else {
            return "NO";
        }
    }

    public String getEligibility(int rollNumber) {
        return studentRepository.findById(rollNumber)
                .map(Student::getEligibility)
                .orElse("NA");
    }
}
