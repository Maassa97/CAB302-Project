package com.cab302.javafxreadingdemo.model;

/**
 * Model class representing a Grade (out of 7).
 */
public class Grade {
    //fields
    private int id;        // Database primary key
    private double value;  // Grade value (0–7)

    public Grade(double value) {
        this.value = value;
    }

    public Grade(int id, double value) {
        this.id = id;
        this.value = value;
    }

    // Getter/setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Grade{id=%d, value=%.2f}", id, value);
    }
}
