package com.cab302.javafxreadingdemo;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import atlantafx.base.theme.Dracula;
import atlantafx.base.theme.CupertinoLight;

public class
HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 420, 520);
        stage.setTitle("Sign in");

        scene.getStylesheets().add(
                HelloApplication.class.getResource("/style.css").toExternalForm()
        );

        stage.setTitle("Cal-Calc!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
