package com.cab302.javafxreadingdemo.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteGradeDAO implements IGradeDAO {

    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS grades (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "value REAL NOT NULL)";

    private static final String INSERT = "INSERT INTO grades (value) VALUES (?)";
    private static final String DELETE = "DELETE FROM grades WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM grades";

    public SqliteGradeDAO() {
        try (Statement stmt = SqliteConnection.getInstance().createStatement()) {
            stmt.execute(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addGrade(Grade grade) {
        try (PreparedStatement ps = SqliteConnection.getInstance().prepareStatement(INSERT)) {
            ps.setDouble(1, grade.getValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteGrade(int id) {
        try (PreparedStatement ps = SqliteConnection.getInstance().prepareStatement(DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Grade> getAllGrades() {
        List<Grade> grades = new ArrayList<>();
        try (Statement stmt = SqliteConnection.getInstance().createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                Grade g = new Grade(rs.getDouble("value"));
                g.setId(rs.getInt("id"));
                grades.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public double calculateAverage() {
        List<Grade> grades = getAllGrades();
        if (grades.isEmpty()) return 0;
        double sum = grades.stream().mapToDouble(Grade::getValue).sum();
        return sum / grades.size();
    }
}
