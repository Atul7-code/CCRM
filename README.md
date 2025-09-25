# Campus Course & Records Manager (CCRM)

This is a console-based Java application for managing students, courses, enrollments, and grades for an academic institute. This project was built to demonstrate a wide range of Java SE features and Object-Oriented Programming principles.

## How to Run the Project

1.  **Prerequisites:** JDK 17 (or newer), Apache Maven.
2.  **Clone the repository:** 
3.  **Navigate to the project directory:** `cd ccrm-app`
4.  **Compile the project:** `mvn compile`
5.  **Run the application:** `mvn exec:java -Dexec.mainClass="edu.ccrm.cli.Main"`

---
## Technical Showcase

### Evolution of Java

* **1996 - JDK 1.0:** The initial public release of Java.
* **2004 - J2SE 5.0:** A major release that introduced fundamental features like generics, enums, annotations, and the `Scanner` class.
* **2014 - Java SE 8:** A landmark release that brought functional programming to Java with Lambdas and the Stream API, plus a new Date/Time API.
* **2021 - Java SE 17 (LTS):** The modern long-term support release, solidifying recent features and ensuring stability for enterprise applications.

### Java ME vs SE vs EE

| Feature    | Java ME (Micro Edition) | Java SE (Standard Edition)      | Java EE (Enterprise Edition)   |
| ---------- | ----------------------- | ------------------------------- | ------------------------------ |
| **Target** | Embedded, mobile devices | Desktop, servers, console apps | Large-scale, distributed web apps |
| **Core API** | Subset of Java SE API | Core Java language and libraries | Superset of Java SE API        |
| **Example** | Old feature phone apps  | This CCRM project               | Banking websites, e-commerce sites |

### Java Architecture: JDK, JRE, JVM

* **JVM (Java Virtual Machine):** An abstract machine that provides the runtime environment to execute Java bytecode. This is what makes Java platform-independent ("write once, run anywhere").
* **JRE (Java Runtime Environment):** The software package containing the JVM and standard libraries. It's the minimum required to *run* a Java application.
* **JDK (Java Development Kit):** The full developer kit. It includes the JRE plus tools needed to *write* and compile Java code, such as the `javac` compiler.

---
## My Setup and Application Screenshots

### Java Version Verification

![img.png](img.png)

### IDE Project Setup

![img_1.png](img_1.png)
![img_2.png](img_2.png)

---
## Mapping of Requirements to Code

This table shows where each key technical requirement from the project PDF was implemented.

| Requirement                  | File(s) / Location(s)                                   |
| ---------------------------- | ------------------------------------------------------- |
| **Packages** | `edu.ccrm.cli`, `domain`, `service`, `io`, `config`, `exception` |
| **Abstraction** | `Person.java` (abstract class)                          |
| **Inheritance** | `Student.java` (extends Person)                         |
| **Polymorphism** | `getProfile()` in `Person`/`Student`, `toString()` overrides, `viewTranscript` method. |
| **Encapsulation** | All domain classes use private fields and public getters/setters. |
| **Enums with Fields** | `Grade.java` (contains grade points)                    |
| **NIO.2 File I/O** | `DataExportService.java`, `DataImportService.java`, `BackupService.java`          |
| **Streams & Lambdas** | `TranscriptService.java` (for GPA & top students), `BackupService.java` |
| **Recursion** | `BackupService.java` (in the `calculateDirectorySize` method) |
| **Singleton Pattern** | `AppConfig.java`                                        |
| **Builder Pattern** | `Course.java` (contains a static nested Builder class)  |
| **Custom Exceptions** | `DuplicateEnrollmentException.java`, `MaxCreditLimitExceededException.java` |
