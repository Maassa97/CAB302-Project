package com.cab302.javafxreadingdemo.controller.Flashcards;

import com.cab302.javafxreadingdemo.HelloApplication;
import com.cab302.javafxreadingdemo.model.Grade;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import com.cab302.javafxreadingdemo.ui.UiMessages;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;

// Deck creator controller class
public class DeckController {

    @FXML private Button finishDeckButton;
    @FXML private TextField deckName;
    @FXML private Label errorLabel;
    @FXML private HBox containerHBox;
    @FXML private Button addCardButon;
    @FXML private Button finishCardButton;;

    boolean cardOpen = false;

    // Create new card with input fields
    @FXML
    private void newCard() {
        if (!cardOpen){
            // make a new vbox
            VBox newVBox = new VBox(10);
            newVBox.setMaxWidth(500);
            newVBox.setMaxHeight(450);
            newVBox.setStyle("-fx-border-color: white; -fx-border-width: 2;");

            // make new gridpane
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(20);

            // column constraints
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setMinWidth(90);
            column1.setPrefWidth(100);
            column1.setMaxWidth(Double.MAX_VALUE);
            column1.setHgrow(Priority.NEVER);
            column1.setFillWidth(true);

            ColumnConstraints column2 = new ColumnConstraints();
            column2.setMinWidth(0);
            column2.setPrefWidth(200);
            column2.setMaxWidth(Double.MAX_VALUE);
            column2.setHgrow(Priority.ALWAYS);
            column2.setFillWidth(true);

            gridPane.getColumnConstraints().addAll(column1, column2);

            // add new label and text box to the gridpane
            TextField cardTitle = new TextField();
            cardTitle.setPromptText("Add Card Title...");
            cardTitle.setStyle("-fx-alignment: center; -fx-font-size: 15;");
            cardTitle.setMinWidth(310);

            TextArea cardBody = new TextArea();
            cardBody.setPromptText("Add Card Text...");
            cardBody.setStyle("-fx-alignment: center; -fx-font-size: 15;");
            cardBody.setWrapText(true);
            cardBody.setPrefHeight(350);
            cardBody.setMinWidth(310);

            gridPane.add(cardTitle, 0, 1);
            gridPane.add(cardBody, 0, 2);

            // add new gridpane to the vbox
            newVBox.getChildren().add(gridPane);

            // add new hbox to the vbox
            containerHBox.getChildren().add(newVBox);

            cardOpen = true;
        }
    }


    @FXML
    private void finishCard() {
        if (containerHBox != null && containerHBox.getChildren().size() > 1) {
            containerHBox.getChildren().removeLast();
         }
        cardOpen = false;
    }


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
            UiMessages.error("Could not close Deck Editor.");
            e.printStackTrace();
        }
    }

    // home screen back button
    @FXML
    private void onBackToHome(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("/com/cab302/javafxreadingdemo/home-view.fxml")
            );
            Parent homeRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(homeRoot);
        } catch (IOException e) {
            UiMessages.error("Failed to open Home screen.");
        }
    }
}