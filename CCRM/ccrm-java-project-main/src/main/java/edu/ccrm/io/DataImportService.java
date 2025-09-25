package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Student;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.StudentService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class DataImportService {
    private final StudentService studentService;
    private final CourseService courseService;
    public DataImportService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public void importAllData() {
        try {
            importStudents();
            importCourses();
            System.out.println("✅ Data imported successfully from the 'data' directory.");
        } catch (IOException e) {
            System.out.println("❌ Error importing data: " + e.getMessage());
        }
    }

    private void importStudents() throws IOException {
        Path studentFile = AppConfig.getInstance().getDataDirectory().resolve("students.csv");
        if (!Files.exists(studentFile)) return;

        List<String> lines = Files.readAllLines(studentFile);
        lines.stream()
                .skip(1)
                .map(line -> line.split(","))
                .forEach(parts -> {
                    int id = Integer.parseInt(parts[0]);
                    String regNo = parts[1];
                    String fullName = parts[2];
                    String email = parts[3];
                    Student student = new Student(id, fullName, email, LocalDate.of(2005, 1, 1), regNo);
                    studentService.addStudent(student);
                });
    }

    private void importCourses() throws IOException {
        Path courseFile = AppConfig.getInstance().getDataDirectory().resolve("courses.csv");
        if (!Files.exists(courseFile)) return;

        List<String> lines = Files.readAllLines(courseFile);
        lines.stream()
                .skip(1)
                .map(line -> line.split(","))
                .forEach(parts -> {
                    String code = parts[0];
                    String title = parts[1];
                    int credits = Integer.parseInt(parts[2]);
                    String instructor = parts[3];
                    Course course = new Course.Builder(code, title)
                            .credits(credits)
                            .instructor(instructor)
                            .build();
                    courseService.addCourse(course);
                });
    }
}