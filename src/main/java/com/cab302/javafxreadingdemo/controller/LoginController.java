package com.cab302.javafxreadingdemo.controller;

import com.cab302.javafxreadingdemo.HelloApplication;
import com.cab302.javafxreadingdemo.model.IUserDAO;
import com.cab302.javafxreadingdemo.model.SqliteUserDAO;
import com.cab302.javafxreadingdemo.model.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;


/** Controller class for login screen
 *
 *  Role:
 *  - handle login attempts
 *  - validate credentials
 *  - authenticate against SQLite DB (using BCrypt hashing)
 *  - navigate to home screen -> if successful
 *  - handle link to new sign-up
 *
 * */
public class LoginController {

    // FXML implemented for login-view
    @FXML private TextField emailField; //email
    @FXML private PasswordField passwordField; //password
    @FXML private Button signInButton; //"Sign up"
    @FXML private Label errorLabel; //label for errors

    @FXML

    /** onSignIn handles user sign in attempt when pressing button
     * 1. validate email/pword input
     * 2. looks up user in SQLite
     * 3. verify pword vs stored BCrypt hash
     * 4. Opens home-view.fxml -> if successful
     */
    private void onSignIn() {

        // User input
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String pass  = passwordField.getText() == null ? "" : passwordField.getText();

        // Basic validation
        if (email.isEmpty() || pass.isEmpty()) {
            showError("Please enter both email and password.");
            return;
        }
        if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            showError("Email format is invalid");
            return;
        }

        // Check DB for user
        IUserDAO userDAO = new SqliteUserDAO();
        User user = userDAO.getUserByEmail(email);

        // Check password with BCrypt
        if (user == null || !org.mindrot.jbcrypt.BCrypt.checkpw(pass, user.getPassword())) {
            showError("Invalid email or password.");
            return;
        }

        // Successful login
        errorLabel.setText("");
        signInButton.setDisable(true);
        signInButton.setText("Signing in…");

        try {
            // Load home screen
            FXMLLoader loader =
                    new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
            Parent root = loader.load();

            // Change from login to home
            Stage stage = (Stage) signInButton.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            // handle invalid FXML elements
            e.printStackTrace();
            showError("Could not open the home screen.");
            signInButton.setDisable(false);
            signInButton.setText("Sign In");
        }

    }

    /**
     * Handles "Sign Up" page
     * */
    @FXML
    private void onSignUp() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(HelloApplication.class.getResource("signup-view.fxml"));
            Parent root = loader.load();

            // replace current scene with sign-up screen
            Stage stage = (Stage) signInButton.getScene().getWindow();
            stage.getScene().setRoot(root);

            // clear any prior error
            errorLabel.setText("");
        } catch (IOException e) {
            // handle invalid FXML elements
            errorLabel.setText("Could not open sign-up screen.");
            e.printStackTrace();
        }
    }

    //placeholder flashcard code. will move to the correct spot eventually
    @FXML private Button flashcardButton;
    @FXML
    private void test()
    {
        try {
            // load Flashcard scene
            FXMLLoader loader =
                    new FXMLLoader(HelloApplication.class.getResource("flashcard-view.fxml"));
            Parent root = loader.load();

            // replace current scene with Flashcard scene
            Stage stage = (Stage) flashcardButton.getScene().getWindow();
            stage.getScene().setRoot(root);

            // clear any prior error
            errorLabel.setText("");
        } catch (IOException e) {
            // handle invalid FXML elements
            errorLabel.setText("Could not open Flashcards.");
            e.printStackTrace();
        }
    }

    // UI error messages
    private void showError(String msg) {
        errorLabel.setText(msg);
    }
}
