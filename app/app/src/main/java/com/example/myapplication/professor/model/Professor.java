package com.example.myapplication.professor.model;

public class Professor {

    private String classroomID;
    private String courseID;
    private String password;
    private String status;
    private String viaID;

    public Professor(String classroomID, String courseID, String password, String status, String viaID) {
        this.classroomID = classroomID;
        this.courseID = courseID;
        this.password = password;
        this.status = status;
        this.viaID = viaID;
    }

    public Professor(){

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

}
