package com.cab302.javafxreadingdemo.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteAssessmentDAO implements AssessmentDAO {

    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS assessments (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " subject_id INTEGER NOT NULL," +
                    " name TEXT NOT NULL," +
                    " weight REAL NOT NULL," +
                    " mark REAL NOT NULL," +
                    " FOREIGN KEY(subject_id) REFERENCES subjects(id) ON DELETE CASCADE" +
                    ")";

    private static final String INSERT =
            "INSERT INTO assessments(subject_id, name, weight, mark) VALUES(?, ?, ?, ?)";

    private static final String SELECT_BY_SUBJECT =
            "SELECT id, subject_id, name, weight, mark FROM assessments WHERE subject_id = ? ORDER BY id";

    private static final String DELETE =
            "DELETE FROM assessments WHERE id = ?";

    private static final String DELETE_BY_SUBJECT =
            "DELETE FROM assessments WHERE subject_id = ?";

    public SqliteAssessmentDAO() {
        try (Statement st = SqliteConnection.getInstance().createStatement()) {
            st.execute(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override public List<Assessment> listBySubject(int subjectId) {
        List<Assessment> out = new ArrayList<>();
        try (PreparedStatement ps = SqliteConnection.getInstance().prepareStatement(SELECT_BY_SUBJECT)) {
            ps.setInt(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Assessment(
                            rs.getInt("id"),
                            rs.getInt("subject_id"),
                            rs.getString("name"),
                            rs.getDouble("weight"),
                            rs.getDouble("mark")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return out;
    }

    @Override public void add(Assessment a) {
        try (PreparedStatement ps = SqliteConnection.getInstance().prepareStatement(INSERT)) {
            ps.setInt(1, a.getSubjectId());
            ps.setString(2, a.getName().trim());
            ps.setDouble(3, a.getWeight());
            ps.setDouble(4, a.getMark());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override public void delete(int assessmentId) {
        try (PreparedStatement ps = SqliteConnection.getInstance().prepareStatement(DELETE)) {
            ps.setInt(1, assessmentId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override public void deleteBySubject(int subjectId) {
        try (PreparedStatement ps = SqliteConnection.getInstance().prepareStatement(DELETE_BY_SUBJECT)) {
            ps.setInt(1, subjectId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
