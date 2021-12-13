package com.example.myapplication.schedulemanager.model;

public class CurrentScheduleManager {
    public static String currentViaID ="";

    public static String getCurrentViaID() {
        return currentViaID;
    }

    public static void setCurrentViaID(String currentViaID) {
        CurrentScheduleManager.currentViaID = currentViaID;
    }
}
