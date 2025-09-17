package com.cab302.javafxreadingdemo.controller.Flashcards;

import com.cab302.javafxreadingdemo.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

// Deck creator controller class
public class DeckController {

    @FXML private Button finishDeckButton;
    @FXML private TextField deckName;
    @FXML private Label errorLabel;

    // OnClick finish deck button
    public void finishDeck()
    {
        try
        {
            // Store user input deck name
            String deckname  = deckName.getText() == null ? "" : deckName.getText();
            System.out.println(deckname);

            // load new flashcard menu scene
            FXMLLoader loader =
                    new FXMLLoader(HelloApplication.class.getResource("flashcard-view.fxml"));
            Parent root = loader.load();

            // replace current scene with flashcard menu scene
            Stage stage = (Stage) finishDeckButton.getScene().getWindow();
            stage.getScene().setRoot(root);

            errorLabel.setText("");
        }
        //error handling
        catch (IOException e)
        {
            errorLabel.setText("Could not Close Deck Editor.");
            e.printStackTrace();
        }
    }
}
