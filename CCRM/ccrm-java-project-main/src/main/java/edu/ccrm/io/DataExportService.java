package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Student;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.StudentService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class DataExportService {
    private final StudentService studentService;
    private final CourseService courseService;
    public DataExportService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public void exportAllData() {
        try {
            Path dataDir = AppConfig.getInstance().getDataDirectory();
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
            exportStudents();
            exportCourses();
            System.out.println("✅ Data exported successfully to the 'data' directory.");
        } catch (IOException e) {
            System.out.println("❌ Error exporting data: " + e.getMessage());
        }
    }

    private void exportStudents() throws IOException {
        Path studentFile = AppConfig.getInstance().getDataDirectory().resolve("students.csv");
        String studentData = studentService.getAllStudents().stream()
                .map(s -> String.join(",", String.valueOf(s.getId()), s.getRegNo(), s.getFullName(), s.getEmail()))
                .collect(Collectors.joining("\n"));
        try (BufferedWriter writer = Files.newBufferedWriter(studentFile)) {
            writer.write("ID,RegNo,FullName,Email\n");
            writer.write(studentData);
        }
    }

    private void exportCourses() throws IOException {
        Path courseFile = AppConfig.getInstance().getDataDirectory().resolve("courses.csv");
        String courseData = courseService.getAllCourses().stream()
                .map(c -> String.join(",", c.getCode(), c.getTitle(), String.valueOf(c.getCredits()), c.getInstructor()))
                .collect(Collectors.joining("\n"));
        try (BufferedWriter writer = Files.newBufferedWriter(courseFile)) {
            writer.write("Code,Title,Credits,Instructor\n");
            writer.write(courseData);
        }
    }
}