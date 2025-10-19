package com.cab302.javafxreadingdemo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mindrot.jbcrypt.BCrypt;

//tests related to password hashing functionality with BCrypt
public class PasswordHashTest {

    @Test
    void hashIsNotRaw() {
        String raw = "Password!123";
        String hash = BCrypt.hashpw(raw, BCrypt.gensalt(10));
        assertNotEquals(raw, hash, "Hash must never equal raw password");
    }

    @Test
    void hashHasBcryptFormat(){
        String raw  = "Password!123";
        String hash = BCrypt.hashpw(raw, BCrypt.gensalt(10));
        assertTrue(hash.startsWith("$2"), "BCrypt hashes start with $2a/$2b/$2y");
        assertEquals(60, hash.length(), "BCrypt hashes are 60 chars long");
    }

    @Test
    void correctPasswordMatchesHash_wrongPasswordDoesNot() {
        String raw = "Password!123";
        String hash = BCrypt.hashpw(raw, BCrypt.gensalt(10));
        assertTrue(BCrypt.checkpw(raw, hash));
        assertFalse(BCrypt.checkpw("totally-wrong", hash));
    }

    @Test
    void hashIsSalted_eachHashIsDifferent() {
        String raw = "Password!123";
        String h1 = BCrypt.hashpw(raw, BCrypt.gensalt(10));
        String h2 = BCrypt.hashpw(raw, BCrypt.gensalt(10));
        assertNotEquals(h1, h2, "Same input should produce different hashes due to salt");
        assertTrue(BCrypt.checkpw(raw, h1));
        assertTrue(BCrypt.checkpw(raw, h2));
    }

    @Test
    void costFactorEmbeddedInHash_isWhatWeAskedFor() {
        String raw = "cost-check";
        String h12 = BCrypt.hashpw(raw, BCrypt.gensalt(12));
        // hash format: $2b$12$<22-char-salt><31-char-hash>
        String cost = h12.split("\\$")[2]; // e.g., "12"
        assertEquals("12", cost);
        assertTrue(BCrypt.checkpw(raw, h12));
    }

    @Test
    void malformedHash_throwsIllegalArgumentException() {
        String raw = "x";
        String bad = "$2balkjeklfawjelkfkjawejf";
        assertThrows(IllegalArgumentException.class, () -> BCrypt.checkpw(raw, bad));
    }
    @Test
    void differentPasswordsProduceDifferentHashes() {
        String h1 = org.mindrot.jbcrypt.BCrypt.hashpw("alpha", org.mindrot.jbcrypt.BCrypt.gensalt(10));
        String h2 = org.mindrot.jbcrypt.BCrypt.hashpw("bravo", org.mindrot.jbcrypt.BCrypt.gensalt(10));
        assertNotEquals(h1, h2);
    }

    @Test
    void verifyIsCaseSensitive() {
        String raw = "PassWORD123";
        String hash = org.mindrot.jbcrypt.BCrypt.hashpw(raw, org.mindrot.jbcrypt.BCrypt.gensalt(10));
        assertTrue(org.mindrot.jbcrypt.BCrypt.checkpw("PassWORD123", hash));
        assertFalse(org.mindrot.jbcrypt.BCrypt.checkpw("password123", hash)); // different case
    }
}

