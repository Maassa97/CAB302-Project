package com.cab302.javafxreadingdemo.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite-backed implementation of IUserDAO.
 * Handles persistence of users into a local SQLite database.
 */
public class SqliteUserDAO implements IUserDAO {

    // SQL statement to create the users table if it does not exist
    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "email TEXT UNIQUE NOT NULL,"
                    + "password TEXT NOT NULL)";

    // Prepared SQL statements for CRUD operations
    private static final String ADD_USER =
            "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
    private static final String GET_USER_BY_EMAIL =
            "SELECT * FROM users WHERE email = ?";
    private static final String GET_ALL_USERS =
            "SELECT * FROM users";


    //Constructor ensures the table exists when DAO is first used.

    public SqliteUserDAO() {
        try (Statement stmt = SqliteConnection.getInstance().createStatement()) {
            stmt.execute(CREATE_USERS_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //Insert a new user into the database.

    @Override
    public void addUser(User user) {
        try (PreparedStatement ps = SqliteConnection.getInstance().prepareStatement(ADD_USER)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve a single user by email.
     * Returns null if no user with that email exists.
     */
    @Override
    public User getUserByEmail(String email) {
        try (PreparedStatement ps = SqliteConnection.getInstance().prepareStatement(GET_USER_BY_EMAIL)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                u.setId(rs.getInt("id")); // assign DB-generated ID
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // not found
    }

    //Retrieve all users in the database.

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement stmt = SqliteConnection.getInstance().createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_USERS)) {
            while (rs.next()) {
                User u = new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                u.setId(rs.getInt("id"));
                users.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
