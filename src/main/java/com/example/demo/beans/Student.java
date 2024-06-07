package com.example.demo.beans;

import javax.persistence.*;


@Entity
@Table(name = "Student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rollNumber")
    private int rollNumber;

    @Column(name = "studentName")
    private String studentName;

    @Column(name = "scienceMarks")
    private double scienceMarks;

    @Column(name = "mathsMarks")
    private double mathsMarks;

    @Column(name = "englishMarks")
    private double englishMarks;

    @Column(name = "computerMarks")
    private double computerMarks;

    public Student() {
        // No-argument constructor required by JPA
    }

    public Student(int rollNumber, String studentName, double scienceMarks, double mathsMarks, double englishMarks, double computerMarks) {
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.scienceMarks = scienceMarks;
        this.mathsMarks = mathsMarks;
        this.englishMarks = englishMarks;
        this.computerMarks = computerMarks;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public double getScienceMarks() {
        return scienceMarks;
    }

    public void setScienceMarks(double scienceMarks) {
        this.scienceMarks = scienceMarks;
    }

    public double getMathsMarks() {
        return mathsMarks;
    }

    public void setMathsMarks(double mathsMarks) {
        this.mathsMarks = mathsMarks;
    }

    public double getEnglishMarks() {
        return englishMarks;
    }

    public void setEnglishMarks(double englishMarks) {
        this.englishMarks = englishMarks;
    }

    public double getComputerMarks() {
        return computerMarks;
    }

    public void setComputerMarks(double computerMarks) {
        this.computerMarks = computerMarks;
    }

	public void setEligibility(String string) {
		
		
	}

	public String getEligibility() {
		// TODO Auto-generated method stub
		
	}
}
