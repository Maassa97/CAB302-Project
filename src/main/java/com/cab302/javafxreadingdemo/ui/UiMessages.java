package com.cab302.javafxreadingdemo.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/** Standardized UI alerts. */
public final class UiMessages {
    private UiMessages() {}

    /** Info alert */
    public static void info(String message) {
        show(Alert.AlertType.INFORMATION, message);
    }

    /** Warning alert */
    public static void warn(String message) {
        show(Alert.AlertType.WARNING, message);
    }

    /** Error alert */
    public static void error(String message) {
        show(Alert.AlertType.ERROR, message);
    }

    private static void show(Alert.AlertType type, String message) {
        Alert a = new Alert(type, message, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
