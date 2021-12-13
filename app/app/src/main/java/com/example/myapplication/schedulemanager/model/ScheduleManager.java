package com.example.myapplication.schedulemanager.model;

public class ScheduleManager {

    private String password;
    private String status;
    private String viaID;

    public ScheduleManager(String password, String status, String viaID) {
        this.password = password;
        this.status = status;
        this.viaID = viaID;
    }

    public ScheduleManager(){

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

    public String getViaID() {
        return viaID;
    }

    public void setViaID(String viaID) {
        this.viaID = viaID;
    }
}
