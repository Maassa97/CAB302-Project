package com.cab302.javafxreadingdemo.model;

/**
 * Model class representing a User in the system.
 * Encapsulates user data with private fields and public getters/setters.
 */
public class User {
    private int id;          // Unique ID (auto-incremented in DB)
    private String name;     // Display name of the user
    private String email;    // Email used for login (must be unique)
    private String password; // Password (currently plain-text for demo)

    /**
     * Constructor to create a new User object before persisting to DB.
     */
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getter + Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
