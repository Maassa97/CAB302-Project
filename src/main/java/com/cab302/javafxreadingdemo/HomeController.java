package com.cab302.javafxreadingdemo;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import atlantafx.base.theme.Dracula;
//import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.CupertinoLight;

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

    //toggle

    @FXML private ToggleButton lightBtn;
    @FXML private ToggleButton darkBtn;

    @FXML
    private void initialize() {
        ToggleGroup themeGroup = new ToggleGroup();
        lightBtn.setToggleGroup(themeGroup);
        darkBtn.setToggleGroup(themeGroup);

        themeGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == null) return;
            ToggleButton btn = (ToggleButton) newT;
            if ("Light".equals(btn.getText())) {
                Application.setUserAgentStylesheet(
                        new CupertinoLight().getUserAgentStylesheet()
                );
            } else {
                Application.setUserAgentStylesheet(
                        new Dracula().getUserAgentStylesheet()
                );
            }
        });
    }


}

