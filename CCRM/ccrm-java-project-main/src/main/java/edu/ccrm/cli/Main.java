package edu.ccrm.cli;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Student;
import edu.ccrm.exception.DuplicateEnrollmentException;
import edu.ccrm.io.BackupService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.TranscriptService;
import edu.ccrm.io.DataExportService;
import edu.ccrm.io.DataImportService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final StudentService studentService = new StudentService();
    private static final CourseService courseService = new CourseService();
    private static final EnrollmentService enrollmentService = new EnrollmentService(studentService, courseService);
    private static final TranscriptService transcriptService = new TranscriptService(studentService); // <-- Added new service
    private static final DataExportService dataExportService = new DataExportService(studentService, courseService);
    private static final DataImportService dataImportService = new DataImportService(studentService, courseService);
    private static final BackupService backupService = new BackupService();

    public static void main(String[] args) {
        System.out.println("Welcome to the Campus Course & Records Manager (CCRM)");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: showStudentMenu(scanner); break;
                case 2: showCourseMenu(scanner); break;
                case 3: showEnrollmentMenu(scanner); break;
                case 4: showFileMenu(scanner); break;
                case 5: showReportsMenu(scanner); break;
                case 6: running = false; break;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
        printPlatformNote();
        System.out.println("Thank you for using CCRM!");
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Courses");
        System.out.println("3. Manage Enrollment & Grades");
        System.out.println("4. File Utilities");
        System.out.println("5. Reports");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }


    private static void showFileMenu(Scanner scanner) {
        boolean inFileMenu = true;
        while(inFileMenu) {
            printFileMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    dataExportService.exportAllData();
                    break;
                case 2:
                    dataImportService.importAllData();
                    break;
                case 3:
                    backupService.performBackup();
                    break;
                case 4:
                    backupService.displayLatestBackupSize();
                    break;
                case 5:
                    inFileMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void printFileMenu() {
        System.out.println("\n--- File Utilities ---");
        System.out.println("1. Export Data to Files");
        System.out.println("2. Import Data from Files");
        System.out.println("3. Create a Backup");
        System.out.println("4. Show Latest Backup Size");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }



    private static void showEnrollmentMenu(Scanner scanner) {
        boolean inEnrollmentMenu = true;
        while (inEnrollmentMenu) {
            printEnrollmentMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1: enrollStudent(scanner); break;
                case 2: recordGrade(scanner); break;
                case 3: viewTranscript(scanner); break;
                case 4: listAllEnrollments(); break;
                case 5: inEnrollmentMenu = false; break;
                default: System.out.println("Invalid option.");
            }
        }
    }
    private static void printEnrollmentMenu() {
        System.out.println("\n--- Enrollment & Grade Management ---");
        System.out.println("1. Enroll Student in Course");
        System.out.println("2. Record a Grade");
        System.out.println("3. View Student Transcript");
        System.out.println("4. List All Enrollments (Debug)");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private static void viewTranscript(Scanner scanner) {
        System.out.println("\n--- View Student Transcript ---");
        System.out.print("Enter student's registration number: ");
        String regNo = scanner.nextLine();
        Optional<Student> studentOpt = studentService.findStudentByRegNo(regNo);
        if (studentOpt.isEmpty()) {
            System.out.println("❌ Error: Student not found.");
            return;
        }
        Student student = studentOpt.get();
        System.out.println("\n--- Transcript for " + student.getFullName() + " ---");
        System.out.println("Registration No: " + student.getRegNo());
        System.out.println("------------------------------------");
        if (student.getEnrollments().isEmpty()) {
            System.out.println("No courses enrolled.");
        } else {
            student.getEnrollments().forEach(enrollment -> {
                String grade = (enrollment.getGrade() == null) ? "Not Graded" : enrollment.getGrade().toString();
                System.out.printf("Course: %s (%s) | Grade: %s\n",
                        enrollment.getCourse().getTitle(),
                        enrollment.getCourse().getCode(),
                        grade);
            });
        }
        double gpa = transcriptService.calculateGpa(regNo);
        System.out.println("------------------------------------");
        System.out.printf("Cumulative GPA: %.2f\n", gpa);
    }

    // --- All other methods remain below ---
    private static void enrollStudent(Scanner scanner) {
        System.out.println("\n--- Enroll Student in Course ---");
        System.out.print("Enter student's registration number: ");
        String regNo = scanner.nextLine();
        System.out.print("Enter course code to enroll in: ");
        String courseCode = scanner.nextLine();

        try {
            boolean success = enrollmentService.enrollStudentInCourse(regNo, courseCode);
            if (success) {
                System.out.println("✅ Enrollment successful!");
            } else {
                System.out.println("❌ Enrollment failed: Student or Course not found.");
            }
        } catch (DuplicateEnrollmentException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void recordGrade(Scanner scanner) {
        System.out.println("\n--- Record a Grade ---");
        try {
            System.out.print("Enter student's registration number: ");
            String regNo = scanner.nextLine();
            System.out.print("Enter course code: ");
            String courseCode = scanner.nextLine();
            System.out.print("Enter grade (S, A, B, C, D, E, F): ");
            String gradeStr = scanner.nextLine().toUpperCase();
            Grade grade = Grade.valueOf(gradeStr);
            boolean success = enrollmentService.assignGrade(regNo, courseCode, grade);
            if (success) {
                System.out.println("✅ Grade recorded successfully!");
            } else {
                System.out.println("❌ Failed to record grade. Enrollment not found.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: Invalid grade entered. Please use S, A, B, etc.");
        }
    }
    private static void showStudentMenu(Scanner scanner) {
        boolean inStudentMenu = true;
        while (inStudentMenu) {
            printStudentMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1: addStudent(scanner); break;
                case 2: listStudents(); break;
                case 3: updateStudent(scanner); break;
                case 4: inStudentMenu = false; break;
                default: System.out.println("Invalid option.");
            }
        }
    }
    private static void printStudentMenu() {
        System.out.println("\n--- Student Management ---");
        System.out.println("1. Add New Student");
        System.out.println("2. List All Students");
        System.out.println("3. Update Student");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }
    private static void showCourseMenu(Scanner scanner) {
        boolean inCourseMenu = true;
        while (inCourseMenu) {
            printCourseMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1: addCourse(scanner); break;
                case 2: listCourses(); break;
                case 3: updateCourse(scanner); break;
                case 4: inCourseMenu = false; break;
                default: System.out.println("Invalid option.");
            }
        }
    }
    private static void listAllEnrollments() {
        System.out.println("\n--- All System Enrollments (Debug) ---");
        List<Enrollment> allEnrollments = enrollmentService.getAllEnrollments();

        if (allEnrollments.isEmpty()) {
            System.out.println("There are no enrollments in the system.");
        } else {
            allEnrollments.forEach(enrollment -> {
                String studentInfo = enrollment.getStudent().getFullName();
                String courseInfo = enrollment.getCourse().getTitle();
                String gradeInfo = (enrollment.getGrade() == null) ? "NO GRADE ASSIGNED" : enrollment.getGrade().toString();

                System.out.printf("Student: %s | Course: %s | Grade: %s\n", studentInfo, courseInfo, gradeInfo);
            });
        }
    }
    private static void printCourseMenu() {
        System.out.println("\n--- Course Management ---");
        System.out.println("1. Add New Course");
        System.out.println("2. List All Courses");
        System.out.println("3. Update Course");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private static void addCourse(Scanner scanner) {
        System.out.println("\n--- Add New Course ---");
        try {
            System.out.print("Enter course code (e.g., CS101): ");
            String code = scanner.nextLine();
            System.out.print("Enter course title: ");
            String title = scanner.nextLine();
            System.out.print("Enter credits: ");
            int credits = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter instructor name: ");
            String instructor = scanner.nextLine();
            Course newCourse = new Course.Builder(code, title)
                    .credits(credits)
                    .instructor(instructor)
                    .build();
            courseService.addCourse(newCourse);
            System.out.println("✅ Course added successfully!");
        } catch (Exception e) {
            System.out.println("❌ Error: Invalid input. Please try again.");
        }
    }
    private static void listCourses() {
        System.out.println("\n--- All Courses ---");
        List<Course> allCourses = courseService.getAllCourses();
        if (allCourses.isEmpty()) { System.out.println("No courses have been added yet."); }
        else { allCourses.stream().forEach(course -> System.out.printf("Code: %s, Title: %s, Credits: %d, Instructor: %s\n", course.getCode(), course.getTitle(), course.getCredits(), course.getInstructor())); }
    }
    private static void updateCourse(Scanner scanner) {
        System.out.println("\n--- Update Course ---");
        System.out.print("Enter course code to update: "); String code = scanner.nextLine();
        if (courseService.findCourseByCode(code).isEmpty()) { System.out.println("❌ Error: Course with that code not found."); return; }
        System.out.print("Enter new title: "); String newTitle = scanner.nextLine();
        System.out.print("Enter new instructor: "); String newInstructor = scanner.nextLine();
        if (courseService.updateCourse(code, newTitle, newInstructor)) { System.out.println("✅ Course details updated successfully!"); }
    }
    private static void addStudent(Scanner scanner) {
        System.out.println("\n--- Add New Student ---");
        try {
            System.out.print("Enter full name: "); String name = scanner.nextLine();
            System.out.print("Enter email: "); String email = scanner.nextLine();
            System.out.print("Enter registration number (e.g., S001): "); String regNo = scanner.nextLine();
            int id = studentService.getAllStudents().size() + 1;
            LocalDate dob = LocalDate.of(2005, 1, 1);
            Student newStudent = new Student(id, name, email, dob, regNo);
            studentService.addStudent(newStudent);
            System.out.println("✅ Student added successfully!");
        } catch (Exception e) { System.out.println("❌ Error: Invalid input. Please try again."); }
    }
    private static void listStudents() {
        System.out.println("\n--- All Students ---");
        List<Student> allStudents = studentService.getAllStudents();
        if (allStudents.isEmpty()) { System.out.println("No students have been added yet."); }
        else { allStudents.stream().forEach(student -> System.out.println(student.getProfile())); }
    }
    private static void updateStudent(Scanner scanner) {
        System.out.println("\n--- Update Student ---");
        System.out.print("Enter registration number of student to update: "); String regNo = scanner.nextLine();
        if (studentService.findStudentByRegNo(regNo).isEmpty()) { System.out.println("❌ Error: Student with that registration number not found."); return; }
        System.out.print("Enter new full name: "); String newName = scanner.nextLine();
        System.out.print("Enter new email: "); String newEmail = scanner.nextLine();
        if (studentService.updateStudent(regNo, newName, newEmail)) { System.out.println("✅ Student details updated successfully!"); }
    }
    private static void showReportsMenu(Scanner scanner) {
        boolean inReportsMenu = true;
        while(inReportsMenu) {
            printReportsMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    viewTopStudents(scanner);
                    break;
                case 2:
                    inReportsMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void printReportsMenu() {
        System.out.println("\n--- Reports ---");
        System.out.println("1. View Top Students");
        System.out.println("2. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private static void printPlatformNote() {
        System.out.println("\n--- Platform Summary ---");
        System.out.println("This application is built on Java SE (Standard Edition), the platform for desktop and server applications.");
        System.out.println("Java ME (Micro Edition) is for small, embedded devices.");
        System.out.println("Java EE (Enterprise Edition) is for large-scale, distributed web applications.");
        System.out.println("------------------------");
    }


    private static void viewTopStudents(Scanner scanner) {
        System.out.print("How many top students would you like to see? ");
        int count = scanner.nextInt();
        scanner.nextLine();

        List<Student> topStudents = transcriptService.getTopStudents(count);

        System.out.printf("\n--- Top %d Students by GPA ---\n", count);
        if (topStudents.isEmpty()) {
            System.out.println("No student data to generate a report.");
        } else {
            for (int i = 0; i < topStudents.size(); i++) {
                Student student = topStudents.get(i);
                double gpa = transcriptService.calculateGpa(student.getRegNo());
                System.out.printf("%d. %s (Reg No: %s) - GPA: %.2f\n",
                        i + 1, student.getFullName(), student.getRegNo(), gpa);
            }
        }
    }
}