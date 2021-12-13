package com.example.myapplication.professor.model;

public class CurrentProfessor {
    public static String currentViaID ="";

    public static String currentProfessorCourse = "";

    public static String getCurrentViaID() {
        return currentViaID;
    }

    public static void setCurrentViaID(String currentViaID) {
        CurrentProfessor.currentViaID = currentViaID;
    }

    public static String getCurrentProfessorCourse() {
        return currentProfessorCourse;
    }

    public static void setCurrentProfessorCourse(String currentProfessorCourse) {
        CurrentProfessor.currentProfessorCourse = currentProfessorCourse;
    }
}
