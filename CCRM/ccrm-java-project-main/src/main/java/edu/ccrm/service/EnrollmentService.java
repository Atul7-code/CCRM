package edu.ccrm.service;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import edu.ccrm.domain.Grade;
import edu.ccrm.exception.DuplicateEnrollmentException;


public class EnrollmentService {
    private final StudentService studentService;
    private final CourseService courseService;
    private final List<Enrollment> enrollments = new ArrayList<>();

    public EnrollmentService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public boolean enrollStudentInCourse(String studentRegNo, String courseCode) throws DuplicateEnrollmentException {
        Optional<Student> studentOpt = studentService.findStudentByRegNo(studentRegNo);
        Optional<Course> courseOpt = courseService.findCourseByCode(courseCode);

        if (studentOpt.isPresent() && courseOpt.isPresent()) {
            Student student = studentOpt.get();
            Course course = courseOpt.get();
            boolean alreadyEnrolled = student.getEnrollments().stream()
                    .anyMatch(e -> e.getCourse().getCode().equalsIgnoreCase(courseCode));

            if (alreadyEnrolled) {
                throw new DuplicateEnrollmentException("Student " + studentRegNo + " is already enrolled in course " + courseCode);
            }

            Enrollment newEnrollment = new Enrollment(student, course);
            this.enrollments.add(newEnrollment);
            student.addEnrollment(newEnrollment);
            course.addEnrollment(newEnrollment);

            return true;
        }
        return false;
    }
    public boolean assignGrade(String studentRegNo, String courseCode, Grade grade) {
        Optional<Enrollment> enrollmentOpt = findEnrollment(studentRegNo, courseCode);

        if (enrollmentOpt.isPresent()) {
            enrollmentOpt.get().setGrade(grade);
            return true;
        }
        return false;
    }
    public Optional<Enrollment> findEnrollment(String studentRegNo, String courseCode) {
        return enrollments.stream()
                .filter(e -> e.getStudent().getRegNo().equalsIgnoreCase(studentRegNo) &&
                        e.getCourse().getCode().equalsIgnoreCase(courseCode))
                .findFirst();
    }
    public List<Enrollment> getAllEnrollments() {
        return this.enrollments;
    }
}