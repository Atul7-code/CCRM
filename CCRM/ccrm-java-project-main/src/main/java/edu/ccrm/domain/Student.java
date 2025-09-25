package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String regNo;
    private List<Enrollment> enrollments;

    public Student(int id, String fullName, String email, LocalDate dateOfBirth, String regNo) {
        super(id, fullName, email, dateOfBirth);
        this.regNo = regNo;
        this.enrollments = new ArrayList<>();
    }

    @Override
    public String getProfile() {
        return "Student Profile: " + this.fullName + " (Reg No: " + this.regNo + ")";
    }
    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }
}