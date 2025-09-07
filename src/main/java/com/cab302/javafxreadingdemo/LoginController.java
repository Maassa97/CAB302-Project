package com.cab302.javafxreadingdemo;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button signInButton;
    @FXML private Label errorLabel;

    @FXML
    private void onSignIn() {

        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String pass  = passwordField.getText() == null ? "" : passwordField.getText();

        if (email.isEmpty() || pass.isEmpty()) {
            showError("Please enter both email and password.");
            return;
        }
        if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            showError("Email format is invalid");
            return;
        }

        //dummy login success
        errorLabel.setText("");
        signInButton.setDisable(true);
        signInButton.setText("Signing in…");

    }

    @FXML
    private void onSignUp() {
        errorLabel.setText("Sign up clicked (stub).");
    }


    private void showError(String msg) {
        errorLabel.setText(msg);
    }
}
