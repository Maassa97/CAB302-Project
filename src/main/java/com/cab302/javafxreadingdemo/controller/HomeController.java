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
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.application.Platform;
import java.util.concurrent.CompletableFuture;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.io.InputStream;
import java.util.Properties;

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
@FXML private StackPane rootStack;
@FXML private Region    contentRoot;
@FXML private Label streakLabel;                   // will be injected from FXML

    private final BadgerClient badger = BadgerClient.fromProperties();

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
        // group the toggles
        ToggleGroup themeGroup = new ToggleGroup();
        lightBtn.setToggleGroup(themeGroup);
        darkBtn.setToggleGroup(themeGroup);

        // handle theme changes
        themeGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == null) return;
            ToggleButton btn = (ToggleButton) newT;
            Application.setUserAgentStylesheet(
                    "Light".equals(btn.getText())
                            ? new CupertinoLight().getUserAgentStylesheet()
                            : new Dracula().getUserAgentStylesheet()
            );
        });

        // 🔽 DO THIS ON LOAD (not inside the listener)
        System.out.println("Home init: userId=" + Session.getCurrentUserId()
                + ", labelNull=" + (streakLabel == null));
        refreshStreak();

        // optional: re-read shortly after to allow event processing
        PauseTransition t = new PauseTransition(Duration.seconds(1.5));
        t.setOnFinished(e -> refreshStreak());
        t.play();
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

    private String readBadgeId() {
        try (var in = getClass().getResourceAsStream("/app.properties")) {
            var p = new java.util.Properties();
            p.load(in);
            return p.getProperty("badger.badgeId", "login_streak");
        } catch (Exception e) { return "login_streak"; }
    }


        private void refreshStreak() {
            String userId  = Session.getCurrentUserId();
            String badgeId = readBadgeId();

            CompletableFuture.supplyAsync(() -> {
                try { return badger.getStreak(userId, badgeId); }
                catch (Exception e) { e.printStackTrace(); return -1; }
            }).thenAccept(streak -> Platform.runLater(() -> {
                if (streak >= 0) streakLabel.setText("🔥 " + streak + "-day streak");
                else streakLabel.setText("Streak: unavailable");
            }));

        }

}

