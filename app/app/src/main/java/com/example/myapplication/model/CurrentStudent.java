package com.example.myapplication.model;

public class CurrentStudent {
    public static String currentViaID ="";

    public static String getCurrentViaID() {
        return currentViaID;
    }

    public static void setCurrentViaID(String currentViaID) {
        CurrentStudent.currentViaID = currentViaID;
    }
}
