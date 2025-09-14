package com.cab302.javafxreadingdemo.controller;

import com.cab302.javafxreadingdemo.HelloApplication;
import com.cab302.javafxreadingdemo.model.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/**
 * Controller for the Sign-up screen.
 * Handles form validation and persistence of a new user in the database.
 */
public class SignupController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private Label errorLabel;
    @FXML private Button createButton;

    private final IUserDAO userDAO = new SqliteUserDAO();

    @FXML
    private void onCreateAccount() {
        String name = safe(nameField.getText());
        String email = safe(emailField.getText());
        String pass = safe(passwordField.getText());
        String confirm = safe(confirmField.getText());

        // --- validation ---
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            showError("Please fill out all fields.");
            return;
        }
        if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            showError("That email doesn't look right.");
            return;
        }
        if (pass.length() < 8) {
            showError("Password must be at least 8 characters.");
            return;
        }
        if (!pass.equals(confirm)) {
            showError("Passwords do not match.");
            return;
        }
        if (userDAO.getUserByEmail(email) != null) {
            showError("That email is already registered.");
            return;
        }

        // --- save to DB ---
        User newUser = new User(name, email, pass);
        userDAO.addUser(newUser);

        // clear error, disable button
        showError("");
        createButton.setDisable(true);
        createButton.setText("Account created!");

        // navigate back to login
        try {
            navigate("login-view.fxml");
        } catch (IOException e) {
            showError("Could not open login screen.");
            createButton.setDisable(false);
            createButton.setText("Create Account");
        }
    }

    @FXML
    private void onBackToSignIn() {
        try {
            navigate("login-view.fxml");
        } catch (IOException e) {
            showError("Could not open login screen.");
        }
    }

    private void navigate(String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(HelloApplication.class.getResource(fxmlName),
                        fxmlName + " not found under resources/com/cab302/javafxreadingdemo/")
        );
        Parent root = loader.load();
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    private void showError(String msg) {
        errorLabel.setText(msg == null ? "" : msg);
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
