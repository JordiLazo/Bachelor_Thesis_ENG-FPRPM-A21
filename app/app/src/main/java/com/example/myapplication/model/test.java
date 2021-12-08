package com.example.myapplication.model;

public class test {
    public static void main(String[] args) {
        String[] x = {"jordi","lazo"};
        String[] y = {"C03.11","08-12-2021"};
        Student z = new Student(y,x,"lazo","online","32321");

        System.out.println(z.toString());
    }
}
