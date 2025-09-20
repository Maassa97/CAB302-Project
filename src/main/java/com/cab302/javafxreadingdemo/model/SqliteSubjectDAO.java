package com.cab302.javafxreadingdemo.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//SQLite implementation of SubjectDAO
public class SqliteSubjectDAO implements SubjectDAO {

    //SQL statements:
    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS subjects (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " name TEXT NOT NULL UNIQUE" +
                    ")";

    private static final String INSERT =
            "INSERT OR IGNORE INTO subjects(name) VALUES(?)";

    private static final String SELECT_ALL =
            "SELECT id, name FROM subjects ORDER BY id";

    private static final String COUNT_ALL =
            "SELECT COUNT(*) FROM subjects";

    //validate table exists
    public SqliteSubjectDAO() {
        try (Statement st = SqliteConnection.getInstance().createStatement()) {
            st.execute(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //fetch all subjects in order of creation
    @Override public List<Subject> listAll() {
        List<Subject> out = new ArrayList<>();
        try (PreparedStatement ps = SqliteConnection.getInstance().prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Subject(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return out;
    }

    //insert subject name (ignore duplicates)
    @Override public void add(String name) {
        try (PreparedStatement ps = SqliteConnection.getInstance().prepareStatement(INSERT)) {
            ps.setString(1, name.trim());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    //count total subjects and validate
    @Override public int countAll() {
        try (PreparedStatement ps = SqliteConnection.getInstance().prepareStatement(COUNT_ALL);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { e.printStackTrace(); return 0; }
    }
}
