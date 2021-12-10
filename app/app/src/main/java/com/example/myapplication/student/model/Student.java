package com.example.myapplication.student.model;

import java.util.Arrays;

public class Student {

    private String password;
    private String viaID;
    private String[] courses;
    private String status;
    private String[] attendance;

    public Student(String[] attendance, String[] courses, String password, String status, String viaID) {
        this.attendance = attendance;
        this.viaID = viaID;
        this.password = password;
        this.courses = courses;
        this.status = status;
    }

    public Student(){

    }

    public String getViaID() {
        return viaID;
    }

    public void setViaID(String viaID) {
        this.viaID = viaID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Student{" +
                "password='" + password + '\'' +
                ", viaID='" + viaID + '\'' +
                ", courses=" + Arrays.toString(courses) +
                ", status='" + status + '\'' +
                '}';
    }
}
