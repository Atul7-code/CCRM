package edu.ccrm.service;

import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Student;

import java.util.List;
import java.util.Optional;

public class TranscriptService {

    private final StudentService studentService;

    public TranscriptService(StudentService studentService) {
        this.studentService = studentService;
    }

    public double calculateGpa(String studentRegNo) {
        Optional<Student> studentOpt = studentService.findStudentByRegNo(studentRegNo);
        if (studentOpt.isEmpty()) {
            return 0.0;
        }

        List<Enrollment> enrollments = studentOpt.get().getEnrollments();

        if (enrollments.isEmpty()) {
            return 0.0;
        }

        double totalPoints = 0;
        int totalCredits = 0;

        for (Enrollment e : enrollments) {
            if (e.getGrade() != null) {
                totalCredits += e.getCourse().getCredits();
                totalPoints += e.getGrade().getGradePoint() * e.getCourse().getCredits();
            }
        }

        return (totalCredits == 0) ? 0.0 : totalPoints / totalCredits;
    }
    public List<Student> getTopStudents(int count) {
        return studentService.getAllStudents().stream()
                .sorted((s1, s2) -> Double.compare(calculateGpa(s2.getRegNo()), calculateGpa(s1.getRegNo())))
                .limit(count)
                .collect(java.util.stream.Collectors.toList());
    }
}