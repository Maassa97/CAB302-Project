package com.cab302.javafxreadingdemo.model;

import java.util.List;

/** Data access contract for assessment
 *
 */
public interface AssessmentDAO {
    //list all assessments for subject
    List<Assessment> listBySubject(int subjectId);
    //add/delete
    void add(Assessment a);
    void delete(int assessmentId);
    void deleteBySubject(int subjectId);
}