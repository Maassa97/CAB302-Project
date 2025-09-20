package com.cab302.javafxreadingdemo.model;

import java.util.List;

//subject contract
public interface SubjectDAO {
    //list
    List<Subject> listAll();
    //add
    void add(String name);
    int countAll();
}