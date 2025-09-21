package com.cab302.javafxreadingdemo.model;

import com.cab302.javafxreadingdemo.model.fakes.InMemoryGradeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Statement;
import java.sql.SQLException;


class GradeDAOTestRealDB {

    private IGradeDAO dao;

    @BeforeEach
    void cleanDb() {
        try (Statement s = SqliteConnection.getInstance().createStatement()) {
            s.execute("DELETE FROM grades");
            s.execute("DELETE FROM users");
            s.execute("DELETE FROM sqlite_sequence WHERE name IN ('grades','users')");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dao = new SqliteGradeDAO();
    }


    @Test
    void average_emptyIsZero() {
        assertEquals(0.0, dao.calculateAverage(), 1e-9);
    }

    @Test
    void addOne_averageEqualsThatOne() {
        dao.addGrade(new Grade(5.0));
        assertEquals(5.0, dao.calculateAverage(), 1e-9);
        assertEquals(1, dao.getAllGrades().size());
    }

    @Test
    void addMany_averageIsMean() {
        dao.addGrade(new Grade(6.0));
        dao.addGrade(new Grade(4.0));
        dao.addGrade(new Grade(5.0));
        assertEquals(5.0, dao.calculateAverage(), 1e-9);
    }

    @Test
    void idsAreAssignedOnInsert_whenZero() {
        Grade g = new Grade(6.0);
        assertEquals(0, g.getId());
        dao.addGrade(g);
        assertTrue(g.getId() > 0, "DAO should assign a new id");
    }

    @Test
    void deleteGrade_updatesAverage() {
        Grade a = new Grade(6.0);
        Grade b = new Grade(3.0);
        dao.addGrade(a);
        dao.addGrade(b);

        dao.deleteGrade(a.getId());
        assertEquals(3.0, dao.calculateAverage(), 1e-9);
        assertEquals(1, dao.getAllGrades().size());
    }

    @Test
    void getAll_returnsCopy_andKeepsInsertionOrder() {
        dao.addGrade(new Grade(1.0));
        dao.addGrade(new Grade(2.0));

        List<Grade> first = dao.getAllGrades();
        assertEquals(2, first.size());
        assertEquals(1.0, first.get(0).getValue(), 1e-9);
        assertEquals(2.0, first.get(1).getValue(), 1e-9);

        // mutate returned list: should NOT affect DAO
        first.clear();
        assertEquals(2, dao.getAllGrades().size(), "DAO must not expose internal storage");
    }
}
