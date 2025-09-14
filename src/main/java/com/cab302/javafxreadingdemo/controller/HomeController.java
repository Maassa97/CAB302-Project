package com.cab302.javafxreadingdemo.controller;

import com.cab302.javafxreadingdemo.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import atlantafx.base.theme.Dracula;
//import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.CupertinoLight;

/**
 * Controller to handle home screen of application
 *
 * Role:
 * - Provide main access point to application functions
 *
 */

// Layout of main home-view.fxml elements
public class HomeController {
@FXML private StackPane rootStack;
@FXML private Region    contentRoot;

    // Log out handler
    @FXML
    //TODO: Add logg out functionality
    private void onLogout() {
        try {
            // Load login view from FXML
            Parent loginRoot = FXMLLoader.load(
                    HelloApplication.class.getResource("login-view.fxml")
            );

            // Replace current scene with login
            Scene scene = rootStack.getScene();
            scene.setRoot(loginRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Theme toggles from home-view.fxml
    @FXML private ToggleButton lightBtn;
    @FXML private ToggleButton darkBtn;

    // Initialises once FXML is loaded
    @FXML
    private void initialize() {
        // Only one button active at once:
        ToggleGroup themeGroup = new ToggleGroup();
        lightBtn.setToggleGroup(themeGroup);
        darkBtn.setToggleGroup(themeGroup);

        // Handle theme changes
        themeGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == null) return;
            ToggleButton btn = (ToggleButton) newT;
            // Switch based on button text
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

    // Handles calendar opening
    @FXML
    private void onOpenCalendar() {
        try {
            Parent calendarRoot = FXMLLoader.load(
                    HelloApplication.class.getResource("calendar-view.fxml")
            );
            Scene scene = rootStack.getScene();
            scene.setRoot(calendarRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

