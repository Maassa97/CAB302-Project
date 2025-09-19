package com.cab302.javafxreadingdemo.model;

import java.util.List;

public interface SubjectDAO {
    List<Subject> listAll();
    void add(String name);
    int countAll();
}