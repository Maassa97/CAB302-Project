package com.cab302.javafxreadingdemo.controller;

import com.cab302.javafxreadingdemo.HelloApplication;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.time.LocalTime;

public class CalendarController {

    @FXML private CalendarView calendarView;

    @FXML
    private void initialize() {
        Calendar goalsCalendar = new Calendar("Goals");
        goalsCalendar.setStyle(Calendar.Style.STYLE1);

        CalendarSource myCalendarSource = new CalendarSource("My Goals");
        myCalendarSource.getCalendars().add(goalsCalendar);

        calendarView.getCalendarSources().add(myCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());
        calendarView.goToday();
        calendarView.showDayPage();
    }

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
            e.printStackTrace();
        }
    }
}