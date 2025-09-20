package com.cab302.javafxreadingdemo.model;

public final class GradeUtils {
    private GradeUtils() {}

    // set percentage for GPA 1-7
    public static int gpaFromPercent(double p) {
        if (p >= 85) return 7;
        if (p >= 75) return 6;
        if (p >= 65) return 5;
        if (p >= 50) return 4;
        if (p >= 45) return 3;
        if (p >= 20) return 2;
        return 1;
    }
}
