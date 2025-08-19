package com.cab302.javafxreadingdemo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;
    // Connor Fan Club Welcome Adjustments
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to ConnorFanClub!");
    }
}