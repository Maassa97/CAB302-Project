package com.example.todolistprototype;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HelloController {

    @FXML
    private VBox rootVBox;

    // for the VBOX on the right hand side
    @FXML
    private VBox buttonListVBox;

    // for the text field!
    @FXML
    private TextField inputTextField;

    @FXML
    private Label dateLabel;


    public void initialize() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        dateLabel.setText(currentDate);
    }

    @FXML
    private void handleAddText() {

        // read the text in the text field box
        String text = inputTextField.getText();
        if (text != null && !text.trim().isEmpty())
        {
            // create a new split menu button and set the button's text
            SplitMenuButton newButton = new SplitMenuButton();
            newButton.setText(text);
            // create a menu item that changes the colour of the button
            MenuItem completedItem = new MenuItem("Mark as completed");
            completedItem.setOnAction(e -> {
                newButton.setStyle("-fx-background-color: green;");
            });
            // create delete menu item
            MenuItem deleteItem = new MenuItem("Remove item");
            deleteItem.setOnAction(e -> buttonListVBox.getChildren().remove(newButton));
            // Add menu items to the split menu button
            newButton.getItems().add(completedItem);
            newButton.getItems().add(deleteItem);
            // Add the new button to the VBox on the right hand side
            buttonListVBox.getChildren().add(newButton);
            // Optionally clear the text field after adding
            inputTextField.clear();
        }
    }



}