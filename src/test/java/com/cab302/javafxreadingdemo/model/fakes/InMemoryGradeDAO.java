package com.cab302.javafxreadingdemo.model.fakes;

import com.cab302.javafxreadingdemo.model.Grade;
import com.cab302.javafxreadingdemo.model.IGradeDAO;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class InMemoryGradeDAO implements IGradeDAO {
    private final Map<Integer, Grade> store = new LinkedHashMap<>();
    private final AtomicInteger seq = new AtomicInteger(0);

    @Override
    public synchronized void addGrade(Grade grade) {
        if (grade.getId() == 0) grade.setId(seq.incrementAndGet());
        store.put(grade.getId(), grade);
    }

    @Override
    public synchronized void deleteGrade(int id) {
        store.remove(id);
    }

    @Override
    public synchronized List<Grade> getAllGrades() {
        return new ArrayList<>(store.values());
    }

    @Override
    public synchronized double calculateAverage() {
        if (store.isEmpty()) return 0.0;
        double sum = 0.0;
        for (Grade g : store.values()) sum += g.getValue();
        return sum / store.size();
    }

    public synchronized void clear() {
        store.clear();
        seq.set(0);
    }
}
