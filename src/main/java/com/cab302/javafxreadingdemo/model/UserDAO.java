package com.cab302.javafxreadingdemo.model;

import java.util.List;

/**
 * DAO (Data Access Object) interface for persisting and retrieving users.
 */
public interface UserDAO {

    /**
     * Add a new user to the database.
     * @param user the user to insert
     */
    void addUser(User user);

    /**
     * Retrieve a user by their email.
     * @param email unique email address
     * @return matching User or null if not found
     */
    User getUserByEmail(String email);

    /**
     * Retrieve all users (useful for testing or admin features).
     * @return a List of all users
     */
    List<User> getAllUsers();
}
