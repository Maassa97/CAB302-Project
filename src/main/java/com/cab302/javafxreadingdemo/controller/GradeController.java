package com.cab302.javafxreadingdemo.controller;

import com.cab302.javafxreadingdemo.HelloApplication;
import com.cab302.javafxreadingdemo.model.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class GradeController {

    @FXML private TextField gradeInput;
    @FXML private TableView<Grade> gradeTable;
    @FXML private TableColumn<Grade, Double> valueColumn;
    @FXML private Label averageLabel;

    private final IGradeDAO gradeDAO = new SqliteGradeDAO();
    private final ObservableList<Grade> gradeList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        valueColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getValue()));
        gradeTable.setItems(gradeList);
        refreshTable();
    }

    @FXML
    private void onAddGrade() {
        try {
            double value = Double.parseDouble(gradeInput.getText().trim());
            if (value < 0 || value > 7) {
                showAlert("Grade must be between 0 and 7.");
                return;
            }
            gradeDAO.addGrade(new Grade(value));
            gradeInput.clear();
            refreshTable();
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid number.");
        }
    }

    @FXML
    private void onBackToHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("/com/cab302/javafxreadingdemo/home-view.fxml")
            );
            Parent homeRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(homeRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeleteGrade() {
        Grade selected = gradeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a grade to delete.");
            return;
        }

        gradeDAO.deleteGrade(selected.getId());
        refreshTable();
    }


    private void refreshTable() {
        gradeList.setAll(gradeDAO.getAllGrades());
        averageLabel.setText(String.format("Average: %.2f", gradeDAO.calculateAverage()));
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
