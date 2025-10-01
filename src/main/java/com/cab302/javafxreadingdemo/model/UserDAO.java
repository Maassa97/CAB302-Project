package com.cab302.javafxreadingdemo.model;

import java.util.List;

/** User DB contract
 *
 */
public interface UserDAO {
    void addUser(User user);
    User getUserByEmail(String email);
    List<User> getAllUsers();
}
