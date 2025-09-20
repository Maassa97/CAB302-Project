package com.cab302.javafxreadingdemo.controller;

import com.cab302.javafxreadingdemo.HelloApplication;
import com.cab302.javafxreadingdemo.model.*;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;
import java.util.OptionalDouble;

/** Grade calculator screen controller
 * Role:
 * -Creates up to 4 subjects
 * -Add/delete assessments for each subject accounting for weight and mark.
 * -Compute % and GPA for subjects and overall
 */
public class GradeController {

    //subject control
    @FXML private TextField subjectNameField;
    @FXML private Button addSubjectBtn;
    @FXML private ComboBox<Subject> subjectCombo;
    @FXML private Label subjectCountLabel;

    //assessment control
    @FXML private TextField assessNameField;
    @FXML private TextField assessWeightField;
    @FXML private TextField assessMarkField;
    @FXML private Button addAssessmentBtn;
    @FXML private Button deleteAssessmentBtn;

    //assessment table control
    @FXML private TableView<Assessment> assessmentTable;
    @FXML private TableColumn<Assessment, String> colName;
    @FXML private TableColumn<Assessment, Double> colWeight;
    @FXML private TableColumn<Assessment, Double> colMark;
    @FXML private TableColumn<Assessment, Double> colContribution;

    //summarised grades (GPA + %) control
    @FXML private Label subjectWeightSum;
    @FXML private Label subjectPercent;
    @FXML private Label subjectGpa;
    @FXML private Label overallPercent;
    @FXML private Label overallGpa;

    //DB access setup
    private final SubjectDAO subjectDAO = new SqliteSubjectDAO();
    private final AssessmentDAO assessmentDAO = new SqliteAssessmentDAO();
    private final ObservableList<Assessment> rows = FXCollections.observableArrayList();

    //4 subject cap
    private static final int SUBJECT_LIMIT = 4;

    //initialise table + load subjects from SQLite
    @FXML
    private void initialize() {
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        colWeight.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getWeight()).asObject());
        colMark.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getMark()).asObject());
        colContribution.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getContribution()).asObject());
        assessmentTable.setItems(rows);

        refreshSubjects();

        //refresh on any changes to subjects
        subjectCombo.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> loadAssessments(n));

        if (!subjectCombo.getItems().isEmpty()) {
            subjectCombo.getSelectionModel().selectFirst();
        } else {
            //show placeholders if no subjects added
            recalcAll();
        }
    }

    // create a new subject (up to 4) make sure name is valid
    @FXML
    private void onAddSubject() {
        if (subjectDAO.countAll() >= SUBJECT_LIMIT) {
            warn("Subject limit reached (4).");
            return;
        }
        String name = safe(subjectNameField.getText());
        if (name.isBlank()) {
            warn("Enter a subject name.");
            return;
        }
        subjectDAO.add(name);
        subjectNameField.clear();
        refreshSubjects();
        //select added subject
        selectSubjectByName(name);
    }

    //add assessment to selected subject
    //validate subject name + weight
    @FXML
    private void onAddAssessment() {
        Subject s = subjectCombo.getValue();
        if (s == null) { warn("Create/select a subject first."); return; }

        String n = safe(assessNameField.getText());
        if (n.isBlank()) { warn("Assessment name required."); return; }

        Double w = parsePercent(assessWeightField.getText(), "Weight");
        if (w == null) return;
        Double m = parsePercent(assessMarkField.getText(), "Mark");
        if (m == null) return;

        assessmentDAO.add(new Assessment(s.getId(), n, w, m));

        assessNameField.clear();
        assessWeightField.clear();
        assessMarkField.clear();

        loadAssessments(s);
    }

    //delete selected subject
    @FXML
    private void onDeleteAssessment() {
        Assessment a = assessmentTable.getSelectionModel().getSelectedItem();
        if (a == null) { warn("Select an assessment to delete."); return; }
        assessmentDAO.delete(a.getId());
        Subject s = subjectCombo.getValue();
        if (s != null) loadAssessments(s);
    }

    //home button
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

    //reload subjects from DB and update stats
    private void refreshSubjects() {
        List<Subject> list = subjectDAO.listAll();
        subjectCombo.getItems().setAll(list);
        subjectCountLabel.setText(list.size() + "/4 subjects");
        recalcOverall(list);
    }

    //select just added subject by name
    private void selectSubjectByName(String name) {
        for (Subject s : subjectCombo.getItems()) {
            if (s.getName().equalsIgnoreCase(name)) {
                subjectCombo.getSelectionModel().select(s);
                break;
            }
        }
    }

    //load assessments for a subject and re-compute stats
    private void loadAssessments(Subject s) {
        rows.clear();
        if (s == null) { recalcAll(); return; }
        rows.addAll(assessmentDAO.listBySubject(s.getId()));
        recalcSubject();
        recalcOverall(subjectCombo.getItems());
    }

    //recalc subject totals from table rows
    private void recalcSubject() {
        double wSum = rows.stream().mapToDouble(Assessment::getWeight).sum();
        double subjPercent = rows.stream().mapToDouble(Assessment::getContribution).sum();
        int subjGpa = GradeUtils.gpaFromPercent(subjPercent);

        subjectWeightSum.setText(String.format("Weight Sum: %.1f%%", wSum));
        subjectPercent.setText(String.format("Subject %%: %.2f%%", subjPercent));
        subjectGpa.setText(String.format("Subject GPA: %d", subjGpa));

        //remind if incomplete input
        if (Math.abs(wSum - 100.0) > 0.0001) {
            subjectWeightSum.setText(subjectWeightSum.getText() + " (not 100%)");
        }
    }

    //recalc overall averages for all subjects
    private void recalcOverall(List<Subject> subjects) {
        if (subjects == null || subjects.isEmpty()) {
            overallPercent.setText("Overall %: --");
            overallGpa.setText("Overall GPA: --");
            return;
        }

        double[] percs = subjects.stream().mapToDouble(s -> {
            List<Assessment> as = assessmentDAO.listBySubject(s.getId());
            if (as.isEmpty()) return Double.NaN;
            return as.stream().mapToDouble(Assessment::getContribution).sum();
        }).filter(d -> !Double.isNaN(d)).toArray();

        if (percs.length == 0) {
            overallPercent.setText("Overall %: --");
            overallGpa.setText("Overall GPA: --");
            return;
        }

        OptionalDouble avg = java.util.Arrays.stream(percs).average();
        double ovp = avg.orElse(0.0);
        int ovg = GradeUtils.gpaFromPercent(ovp);

        overallPercent.setText(String.format("Overall %%: %.2f%%", ovp));
        overallGpa.setText(String.format("Overall GPA: %d", ovg));
    }

    //reset subject laves and compute overall (e.g no subjects)
    private void recalcAll() {
        subjectWeightSum.setText("Weight Sum: --");
        subjectPercent.setText("Subject %: --");
        subjectGpa.setText("Subject GPA: --");
        recalcOverall(subjectCombo.getItems());
    }

    //parse a percentage string [0-100]
    private Double parsePercent(String s, String field) {
        try {
            double v = Double.parseDouble(s.trim());
            if (v < 0 || v > 100) { warn(field + " must be 0–100."); return null; }
            return v;
        } catch (Exception e) { warn("Enter a valid " + field + "."); return null; }
    }

    //warning
    private void warn(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    //trim null
    private String safe(String s) { return s == null ? "" : s.trim(); }
}
