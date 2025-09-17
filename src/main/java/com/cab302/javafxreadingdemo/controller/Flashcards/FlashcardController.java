package com.cab302.javafxreadingdemo.controller.Flashcards;

import com.cab302.javafxreadingdemo.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

    // Flashcards main menu controller

public class FlashcardController
{

    @FXML private Button backButton;
    @FXML private Button newDeckButton;
    @FXML private Button editDeckButton;
    @FXML private Button startPracticeButton;
    @FXML private Label errorLabel;

    public void newDeck()
    {
        try
        {
            // load new deck scene
            FXMLLoader loader =
                    new FXMLLoader(HelloApplication.class.getResource("deck-view.fxml"));
            Parent root = loader.load();

            // replace current scene with new deck scene
            Stage stage = (Stage) newDeckButton.getScene().getWindow();
            stage.getScene().setRoot(root);

            errorLabel.setText("");
        }
        //Error handling
        catch (IOException e)
        {
            errorLabel.setText("Could not open New Deck screen.");
            e.printStackTrace();
        }
    }

    // Back button functionality
    public void back()
    {
        try
        {
            // load Home scene
            FXMLLoader loader =
                    new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
            Parent root = loader.load();

            // replace current scene with Home scene
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.getScene().setRoot(root);

            errorLabel.setText("");
        }
        //error handling
        catch (IOException e)
        {
            errorLabel.setText("Could not open Home Menu Screen.");
            e.printStackTrace();
        }
    }
}
