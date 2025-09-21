package com.cab302.javafxreadingdemo.model;

import com.cab302.javafxreadingdemo.model.fakes.InMemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTestInMemoryDAO {

    private UserDAO dao;

    @BeforeEach
    void setUp() {
        dao = new InMemoryUserDAO();
    }

    @Test
    void addAndFetchByEmail_returnsSameUser() {
        User u = new User("Hazel", "hazel@test.com", "Password!1234");
        dao.addUser(u);

        User got = dao.getUserByEmail("hazel@test.com");
        assertNotNull(got);
        assertEquals("Hazel", got.getName());
        assertEquals("hazel@test.com", got.getEmail());
        assertEquals("Password!1234", got.getPassword());
        assertTrue(got.getId() > 0);
    }

    @Test
    void duplicateEmail_throws() {
        dao.addUser(new User("A", "dup@example.com", "h1"));
        assertThrows(IllegalStateException.class, () ->
                dao.addUser(new User("B", "dup@example.com", "h2"))
        );
    }

    @Test
    void idsAreAssigned_ifZeroOnInsert() {
        User u = new User("X", "x@example.com", "h");
        assertEquals(0, u.getId());
        dao.addUser(u);
        assertTrue(u.getId() > 0);
    }

    @Test
    void getAll_returnsCopy_inInsertionOrder() {
        dao.addUser(new User("A", "a@x", "h"));
        dao.addUser(new User("B", "b@x", "h"));

        List<User> all = dao.getAllUsers();
        assertEquals(2, all.size());
        assertEquals("A", all.get(0).getName());
        assertEquals("B", all.get(1).getName());
        all.clear();
        assertEquals(2, dao.getAllUsers().size());
    }

    @Test
    void nullChecks_guardrails() {
        assertThrows(IllegalArgumentException.class, () -> dao.addUser(null));
        assertThrows(IllegalArgumentException.class, () ->
                dao.addUser(new User("NoEmail", null, "h"))
        );
        assertNull(dao.getUserByEmail("not-found@example.com"));
    }
}
