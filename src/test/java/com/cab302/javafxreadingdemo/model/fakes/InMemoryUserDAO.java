package com.cab302.javafxreadingdemo.model.fakes;

import com.cab302.javafxreadingdemo.model.User;
import com.cab302.javafxreadingdemo.model.UserDAO;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This fake simulates a user DAO but only stores data in memory. It is used for unit testing of user related
 * logic without using a real DB.
 */

public class InMemoryUserDAO implements UserDAO {

    // Keyed by unique email (mirrors DB UNIQUE constraint)
    private final Map<String, User> byEmail = new LinkedHashMap<>();
    private final AtomicInteger seq = new AtomicInteger(0);

    @Override
    public synchronized void addUser(User user) {
        if (user == null) throw new IllegalArgumentException("user == null");
        if (user.getEmail() == null) throw new IllegalArgumentException("email == null");
        if (byEmail.containsKey(user.getEmail())) {

            throw new IllegalStateException("Email already exists: " + user.getEmail());
        }
        if (user.getId() == 0) user.setId(seq.incrementAndGet());
        byEmail.put(user.getEmail(), user);
    }

    @Override
    public synchronized User getUserByEmail(String email) {
        return byEmail.get(email);
    }

    @Override
    public synchronized List<User> getAllUsers() {
        return new ArrayList<>(byEmail.values());
    }

    // Test helper
    public synchronized void clear() {
        byEmail.clear();
        seq.set(0);
    }
}
