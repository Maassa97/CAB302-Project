package com.cab302.javafxreadingdemo.controller;

import com.cab302.javafxreadingdemo.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import com.cab302.javafxreadingdemo.ui.UiMessages;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/** T0-do list controller
 * Role:

 */


public class Todocontroller {

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
        // create table if it doesn't exist
        TableCreator.createTable();
    }

    @FXML
    private void deleteNoteFromDatabase(String content) {
        String deleteSQL = "DELETE FROM notes WHERE content = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, content);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Deleted note: " + content);
            } else {
                System.out.println("Note not found in DB.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleAddText() {

        // read the text in the text field box
        String text = inputTextField.getText();
        if (text != null && !text.trim().isEmpty())
        {
            // connect to database and insert text
            String sql = "INSERT INTO notes(content) VALUES(?)";

            try (Connection conn = DatabaseConnector.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, text);
                pstmt.executeUpdate();
                inputTextField.clear();
            } catch (Exception e) {
                e.printStackTrace();
                return; // stops if DB insert fails
            }
            // create a new split menu button and set the button's text
            SplitMenuButton newButton = new SplitMenuButton();
            newButton.setText(text);
            // create a menu item that changes the colour of the button for mark as completed
            MenuItem completedItem = new MenuItem("Mark as completed");
            completedItem.setOnAction(e -> {
                newButton.setStyle("-fx-background-color: green;");
            });
            // create delete menu item
            MenuItem deleteItem = new MenuItem("Remove item");
            deleteItem.setOnAction(e -> {
                        deleteNoteFromDatabase(text); // Delete from DB
                        buttonListVBox.getChildren().remove(newButton); // Remove from UI
                    });
            // Add menu items to the split menu button
            newButton.getItems().add(completedItem);
            newButton.getItems().add(deleteItem);
            // Add the new button to the VBox on the right hand side
            buttonListVBox.getChildren().add(newButton);
            // Optionally clear the text field after adding
            inputTextField.clear();
        }

    }

    /** home screen back button
     *
     * @param event action for returning to home-screen view
     */
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