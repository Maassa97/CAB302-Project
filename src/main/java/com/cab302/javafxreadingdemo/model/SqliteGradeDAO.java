package com.cab302.javafxreadingdemo.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Sqlite implementation
public class SqliteGradeDAO implements IGradeDAO {

    //create 'Grades' table
    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS grades (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "value REAL NOT NULL)";

    //insert grade into DB
    private static final String INSERT = "INSERT INTO grades (value) VALUES (?)";
    //delete grade
    private static final String DELETE = "DELETE FROM grades WHERE id = ?";
    //select all grades (totals)
    private static final String SELECT_ALL = "SELECT * FROM grades";

    //ensures grade table exists
    public SqliteGradeDAO() {
        try (Statement stmt = SqliteConnection.getInstance().createStatement()) {
            stmt.execute(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //add grade to DB
    @Override
    public void addGrade(Grade grade) {
        try (PreparedStatement ps = SqliteConnection.getInstance().prepareStatement(INSERT)) {
            ps.setDouble(1, grade.getValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //delete grade from DB
    @Override
    public void deleteGrade(int id) {
        try (PreparedStatement ps = SqliteConnection.getInstance().prepareStatement(DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //retrieves all grades in DB
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
    //calculate averages
    @Override
    public double calculateAverage() {
        List<Grade> grades = getAllGrades();
        if (grades.isEmpty()) return 0;
        double sum = grades.stream().mapToDouble(Grade::getValue).sum();
        return sum / grades.size();
    }
}
