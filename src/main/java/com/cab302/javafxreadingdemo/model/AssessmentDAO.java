package com.cab302.javafxreadingdemo.model;

import java.util.List;

public interface AssessmentDAO {
    List<Assessment> listBySubject(int subjectId);
    void add(Assessment a);
    void delete(int assessmentId);
    void deleteBySubject(int subjectId);
}