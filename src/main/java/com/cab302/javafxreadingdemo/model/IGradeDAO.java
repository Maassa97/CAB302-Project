package com.cab302.javafxreadingdemo.model;

import java.util.List;

public interface IGradeDAO {
    void addGrade(Grade grade);
    void deleteGrade(int id);
    List<Grade> getAllGrades();
    double calculateAverage();
}
