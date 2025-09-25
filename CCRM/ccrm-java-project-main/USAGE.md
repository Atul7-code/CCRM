# CCRM Application Usage Guide

This guide provides sample commands and a typical workflow for using the Campus Course & Records Manager.

## How to Run

1.  Compile the project: `mvn compile`
2.  Run the application: `mvn exec:java -Dexec.mainClass="edu.ccrm.cli.Main"`

## Sample Workflow

Here is a sample interactive session to demonstrate the application's features.

**1. Add a Student**
**2. Add a Course**
**3. Enroll Student and Record Grade**
**4. View Transcript**
## Sample Data File Format (`test-data/students.csv`)
```csv
ID,RegNo,FullName,Email
1,S01,Abha Singh,abha@example.com
2,S02,Shivam Sharma,shivam@example.com