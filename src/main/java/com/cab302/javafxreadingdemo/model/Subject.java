package com.cab302.javafxreadingdemo.model;

//group assessments and populate UI
public class Subject {
    private int id;
    private String name;

    public Subject(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Subject(String name) {
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    @Override public String toString() {
        return name;
    }
}
