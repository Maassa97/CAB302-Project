package com.cab302.javafxreadingdemo.controller;

import com.cab302.javafxreadingdemo.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import atlantafx.base.theme.Dracula;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.CupertinoLight;
import javafx.scene.control.Label;
import javafx.application.Platform;
import java.util.concurrent.CompletableFuture;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.io.InputStream;
import java.util.Properties;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.SplitMenuButton;



import com.cab302.javafxreadingdemo.Session;
import com.cab302.javafxreadingdemo.badger.BadgerClient;
/**
 * Controller to handle home screen of application
 *
 * Role:
 * - Provide main access point to application functions
 *
 */

// Layout of main home-view.fxml elements
public class HomeController {
    @FXML
    private StackPane rootStack;
    @FXML
    private Region contentRoot;
    @FXML
    private Label streakLabel;

    private final BadgerClient badger = BadgerClient.fromProperties();

    @FXML
    private SplitMenuButton menuButton;

    @FXML
    private void onLogout(ActionEvent e) {
        //Session.clear();
        try {
            FXMLLoader fx = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
            Parent root = fx.load();

            Stage stage = (Stage) menuButton.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Sign In");
            stage.centerOnScreen();
        } catch (Exception ex) {
            ((Stage) menuButton.getScene().getWindow()).close();
        }
    }

    // Theme toggles from home-view.fxml
    @FXML
    private ToggleButton lightBtn;
    @FXML
    private ToggleButton darkBtn;

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
        refreshStreak();
    }




    //streak refresh
    private void refreshStreak() {
        String userId = Session.getCurrentUserId(); // same helper youÍ› used before
        if (userId == null || userId.isBlank()) {
            streakLabel.setText("Streak: --");
            return;
        }
        String badgeId = readBadgeId();

        java.util.concurrent.CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return badger.getStreak(userId, badgeId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return -1;
                    }
                })
                .thenAccept(streak -> Platform.runLater(() ->
                        streakLabel.setText(streak >= 0 ? ("🔥 " + streak + "-day streak") : "Streak: unavailable")
                ));
    }

    //read badgerID for user
    private String readBadgeId() {
        try (var in = getClass().getResourceAsStream("/app.properties")) {
            var p = new java.util.Properties();
            p.load(in);
            return p.getProperty("badger.badgeId", "streak_store");
        } catch (Exception e) {
            return "streak_store";
        }
    }

    //open grade calculator
    @FXML
    private void onOpenGradeCalculator(ActionEvent e) {
        try {
            Parent gradeRoot = FXMLLoader.load(
                    HelloApplication.class.getResource("grade-calculator.fxml")
            );
            ((Node) e.getSource()).getScene().setRoot(gradeRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void openFlashcard(ActionEvent e) {
        try {
            Parent TodoRoot = FXMLLoader.load(
                    HelloApplication.class.getResource("flashcard-view.fxml")
            );
            ((Node) e.getSource()).getScene().setRoot(TodoRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void opentodolist(ActionEvent e) {
        try {
            Parent TodoRoot = FXMLLoader.load(
                    HelloApplication.class.getResource("Todo-view.fxml")
            );
            ((Node) e.getSource()).getScene().setRoot(TodoRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}