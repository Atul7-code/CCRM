package edu.ccrm.service;
import edu.ccrm.domain.Student;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StudentService {
    private final Map<String, Student> students = new HashMap<>();

    public void addStudent(Student student) {
        students.put(student.getRegNo(), student);
    }

    public Optional<Student> findStudentByRegNo(String regNo) {
        return Optional.ofNullable(students.get(regNo));
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
    public boolean updateStudent(String regNo, String newFullName, String newEmail) {
        Optional<Student> studentOpt = findStudentByRegNo(regNo);
        if (studentOpt.isPresent()) {
            Student studentToUpdate = studentOpt.get();
            studentToUpdate.setFullName(newFullName);
            studentToUpdate.setEmail(newEmail);
            return true;
        }
        return false;
    }
}