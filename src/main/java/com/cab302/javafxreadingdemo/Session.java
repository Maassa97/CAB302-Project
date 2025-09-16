package com.cab302.javafxreadingdemo;

public final class Session {
    private static String currentUserId;
    public static void setCurrentUserId(String id) { currentUserId = id; }
    public static String getCurrentUserId() { return currentUserId; }
    private Session() {}
}
