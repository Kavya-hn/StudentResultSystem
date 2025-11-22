package com.model;

public class Student {
    private int id;
    private String name;
    private int marks1, marks2, marks3;
    private int total;
    private double percentage;
    private String grade;

    public Student() {}

    // Constructor for adding new student
    public Student(String name, int marks1, int marks2, int marks3) {
        this.name = name;
        this.marks1 = marks1;
        this.marks2 = marks2;
        this.marks3 = marks3;
        calculateResults();
    }

    public Student(String name, int marks1, int marks2, int marks3, int total, float percentage, String grade) {
        this.name = name;
        this.marks1 = marks1;
        this.marks2 = marks2;
        this.marks3 = marks3;
        this.total = total;
        this.percentage = percentage;
        this.grade = grade;
    }
    // Constructor for reading from DB
    public Student(int id, String name, int marks1, int marks2, int marks3, int total, double percentage, String grade) {
        this.id = id;
        this.name = name;
        this.marks1 = marks1;
        this.marks2 = marks2;
        this.marks3 = marks3;
        this.total = total;
        this.percentage = percentage;
        this.grade = grade;
    }

    public void calculateResults() {
        this.total = marks1 + marks2 + marks3;
        this.percentage = total / 3.0;
        if (percentage >= 90) grade = "A";
        else if (percentage >= 75) grade = "B";
        else if (percentage >= 60) grade = "C";
        else if (percentage >= 50) grade = "D";
        else grade = "F";
    }

    // Getters and Setters
    public int getId() { 
    	return id; 
    }
    public void setId(int id) {
    	this.id = id; 
    }
    public String getName() { 
    	return name; 
    }
    public void setName(String name) {
    	this.name = name; 
    }
    public int getMarks1() {
    	return marks1; 
    }
    public void setMarks1(int marks1) {
    	this.marks1 = marks1; 
    }
    public int getMarks2() {
    	return marks2; 
    }
    public void setMarks2(int marks2) {
    	this.marks2 = marks2; 
    }
    public int getMarks3() {
    	return marks3; 
    }
    public void setMarks3(int marks3) {
    	this.marks3 = marks3; 
    }
    public int getTotal() {
    	return total; 
    }
    public double getPercentage() { 
    	return percentage; 
    }
    public String getGrade() {
    	return grade; 
    }
}
