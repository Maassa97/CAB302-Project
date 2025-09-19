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

    //FXML CalenderFX Calendar View
    @FXML private CalendarView calendarView;

    //Initialise calendar toolset
    @FXML
    private void initialize() {
        //title
        Calendar goalsCalendar = new Calendar("Goals");

        //use built-in calendar from CalFX
        goalsCalendar.setStyle(Calendar.Style.STYLE1);

        //create calendar source (container)
        CalendarSource myCalendarSource = new CalendarSource("My Goals");

        //add to source
        myCalendarSource.getCalendars().add(goalsCalendar);


        //calendar view set-up
        calendarView.getCalendarSources().add(myCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());
        calendarView.goToday();
        calendarView.showDayPage();
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
            e.printStackTrace();
        }
    }
}