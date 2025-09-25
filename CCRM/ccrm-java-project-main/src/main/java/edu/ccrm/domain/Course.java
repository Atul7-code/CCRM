package edu.ccrm.domain;

import java.util.ArrayList;
import java.util.List;
public class Course {
    private final String code;
    private String title;
    private final int credits;
    private String instructor;
    private List<Enrollment> enrollments;
    private Course(Builder builder) {
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructor = builder.instructor;
        this.enrollments = new ArrayList<>();
    }
    public static class Builder {
        private final String code;
        private final String title;
        private int credits;
        private String instructor = "TBD";

        public Builder(String code, String title) {
            this.code = code;
            this.title = title;
        }

        public Builder credits(int credits) {
            this.credits = credits;
            return this;
        }

        public Builder instructor(String instructor) {
            this.instructor = instructor;
            return this;
        }

        public Course build() {
            return new Course(this);
        }
    }
    public List<Enrollment> getEnrollments() { return enrollments; }
    public void addEnrollment(Enrollment enrollment) { this.enrollments.add(enrollment); }
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public void setTitle(String title) {
        this.title = title;
    }
}