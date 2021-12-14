package com.example.myapplication.schedulemanager.model;

public class CalculateSD {

    public static String calculateSD() {
        double numArray[] = { 23, 24, 30, 25, 10, 8, 6, 3, 10, 25 };
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        float result = (float) Math.sqrt(standardDeviation/length);
        return String.format("%.02f",result);
    }
}
