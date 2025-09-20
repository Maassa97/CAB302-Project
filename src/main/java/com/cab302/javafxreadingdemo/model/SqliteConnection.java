package com.cab302.javafxreadingdemo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//SQLite setup
//JDBC connection

public class SqliteConnection {
    // Static instance of the database connection
    private static Connection instance = null;


    private SqliteConnection() {
        try {
            // Explicitly load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Create or open local SQLite DB file
            instance = DriverManager.getConnection("jdbc:sqlite:users.db");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    // Accessor to create singleton connection
    public static Connection getInstance() {
        if (instance == null) new SqliteConnection();
        return instance;
    }

    // Connection check to database CHECK
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Connected to SQLite!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
