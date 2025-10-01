package com.cab302.javafxreadingdemo.controller;

import com.cab302.javafxreadingdemo.HelloApplication;
import com.cab302.javafxreadingdemo.model.*;
import com.cab302.javafxreadingdemo.ui.UiMessages;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/**
 * Controller for the Sign-up screen
 * Role:
 * - Validate user input (name, email, password, confirmation)
 * - Hash pword with BCrypt
 * - Move new user into database
 * - Navigate back to login screen if successful
 *
 */
public class SignupController {

    // UI elements from signup-view.fxml
    @FXML private TextField nameField; //name
    @FXML private TextField emailField; //email
    @FXML private PasswordField passwordField; //password
    @FXML private PasswordField confirmField; //confirm password
    @FXML private Label errorLabel; //display error
    @FXML private Button createButton; //"Create Account" button

    // DAO for database opertations
    private final UserDAO userDAO = new SqliteUserDAO();

    /** Handles account creation
     * 1. validate inputs
     * 2. ensures unique email
     * 3. hashes pword w/ BCrypt
     * 4. adds user to DB
     * 5. navigates back to login (if successful)
     */
    @FXML
    private void onCreateAccount() {
        // collect valid values (not null,etc)
        String name = safe(nameField.getText());
        String email = safe(emailField.getText());
        String pass = safe(passwordField.getText());
        String confirm = safe(confirmField.getText());

        // Validation
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            UiMessages.warn("Please fill out all fields.");
            return;
        }
        if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            UiMessages.warn("That email doesn't look right.");
            return;
        }
        if (pass.length() < 8) {
            UiMessages.warn("Password must be at least 8 characters.");
            return;
        }
        if (!pass.equals(confirm)) {
            UiMessages.warn("Passwords do not match.");
            return;
        }
        if (userDAO.getUserByEmail(email) != null) {
            UiMessages.warn("That email is already registered.");
            return;
        }

        // Hash password before saving
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(pass, org.mindrot.jbcrypt.BCrypt.gensalt());
        User newUser = new User(name, email, hashedPassword);
        userDAO.addUser(newUser);


        // Success
        showError(""); // Clear error
        createButton.setDisable(true);
        createButton.setText("Account created!");

        // Navigate back to login
        try {
            navigate("login-view.fxml");
        } catch (IOException e) {
            UiMessages.error("Could not open login screen.");
            createButton.setDisable(false);
            createButton.setText("Create Account");
        }
    }

    /** Handles back to sign in upon success
     *
     */
    @FXML
    private void onBackToSignIn() {
        try {
            navigate("login-view.fxml");
        } catch (IOException e) {
            UiMessages.error("Could not open login screen.");
        }
    }

    /** Navigates between FXML screens
     *
     * @param fxmlName path to relevant FXML
     * @throws IOException for error handling
     */
    private void navigate(String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                // error handling
                Objects.requireNonNull(HelloApplication.class.getResource(fxmlName),
                        fxmlName + " not found")
        );
        Parent root = loader.load();
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    /** display error (remove after prototype to dedicated error file)
     *
     * @param msg for error
     */
    private void showError(String msg) {
        errorLabel.setText(msg == null ? "" : msg);
    }

    /** returns empty string if null/trims white space
     *
     * @param s input
     * @return empty if null
     */
    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
