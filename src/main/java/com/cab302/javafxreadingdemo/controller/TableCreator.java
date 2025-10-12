package com.cab302.javafxreadingdemo.controller;

import java.sql.Connection;
import java.sql.Statement;

public class TableCreator {
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "content TEXT NOT NULL" +
                ");";

        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
