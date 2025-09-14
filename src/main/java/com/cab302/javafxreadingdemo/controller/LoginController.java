package com.cab302.javafxreadingdemo.controller;

import com.cab302.javafxreadingdemo.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;


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

        try {
            FXMLLoader loader =
                    new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) signInButton.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Could not open the home screen.");
            signInButton.setDisable(false);
            signInButton.setText("Sign In");
        }

    }




    @FXML
    private void onSignUp() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(HelloApplication.class.getResource("signup-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) signInButton.getScene().getWindow();
            stage.getScene().setRoot(root);
            errorLabel.setText(""); // clear any prior error
        } catch (IOException e) {
            errorLabel.setText("Could not open sign-up screen.");
            e.printStackTrace();
        }
    }




    private void showError(String msg) {
        errorLabel.setText(msg);
    }
}
