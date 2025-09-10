package com.cab302.javafxreadingdemo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class HomeController {
@FXML private StackPane rootStack;
@FXML private Region    contentRoot;


    @FXML
    //TODO: Add logg out functionality
    private void onLogout() {
        try {
            Parent loginRoot = FXMLLoader.load(
                    HelloApplication.class.getResource("login-view.fxml")
            );
            Scene scene = rootStack.getScene();
            scene.setRoot(loginRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
