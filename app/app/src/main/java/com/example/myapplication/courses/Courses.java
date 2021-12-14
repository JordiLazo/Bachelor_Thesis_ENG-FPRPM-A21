package com.example.myapplication.courses;

public class Courses {

    private String active_users;
    private String classroomID;
    private String courseID;
    private String enrolledStudents;
    private String professorID;

    public Courses(String active_users, String classroomID, String courseID, String enrolledStudents, String professorID) {
        this.active_users = active_users;
        this.classroomID = classroomID;
        this.courseID = courseID;
        this.enrolledStudents = enrolledStudents;
        this.professorID = professorID;
    }

    public Courses(){

    }

    public String getActive_users() {
        return active_users;
    }

    public void setActive_users(String active_users) {
        this.active_users = active_users;
    }

    public String getClassroomID() {
        return classroomID;
    }

    public void setClassroomID(String classroomID) {
        this.classroomID = classroomID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(String enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public String getProfessorID() {
        return professorID;
    }

    public void setProfessorID(String professorID) {
        this.professorID = professorID;
    }
}
