package com.cab302.javafxreadingdemo.model;

import java.util.List;

//user contract
public interface UserDAO {
    void addUser(User user);
    User getUserByEmail(String email);
    List<User> getAllUsers();
}
