package com.cab302.javafxreadingdemo.model;

import java.util.List;
//contract for GPA
public interface IGradeDAO {
    //delete/add/get/calc
    void addGrade(Grade grade);
    void deleteGrade(int id);
    List<Grade> getAllGrades();
    double calculateAverage();
}
