package com.cab302.javafxreadingdemo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class to provide a single SQLite database connection.
 * Ensures only one Connection object exists for the whole application.
 */
public class SqliteConnection {
    // Static instance of the database connection
    private static Connection instance = null;

    /**
     * Private constructor that establishes the connection.
     * Called only once when the connection is first requested.
     */
    private SqliteConnection() {
        try {
            // Explicitly load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Create or open local SQLite DB file
            instance = DriverManager.getConnection("jdbc:sqlite:users.db");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found. Did you add it to pom.xml?");
            e.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Public accessor for the connection instance.
     * @return a single shared Connection to the database
     */
    public static Connection getInstance() {
        if (instance == null) new SqliteConnection();
        return instance;
    }

    // connection check to database
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
