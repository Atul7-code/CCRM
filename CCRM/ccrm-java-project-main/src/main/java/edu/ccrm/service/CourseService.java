package edu.ccrm.service;
import edu.ccrm.domain.Course;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CourseService {
    private final Map<String, Course> courses = new HashMap<>();

    public void addCourse(Course course) {
        courses.put(course.getCode(), course);
    }

    public Optional<Course> findCourseByCode(String courseCode) {
        return Optional.ofNullable(courses.get(courseCode));
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    public boolean updateCourse(String courseCode, String newTitle, String newInstructor) {
        Optional<Course> courseOpt = findCourseByCode(courseCode);
        if (courseOpt.isPresent()) {
            Course courseToUpdate = courseOpt.get();
            courseToUpdate.setTitle(newTitle);
            courseToUpdate.setInstructor(newInstructor);
            return true;
        }
        return false;
    }
}