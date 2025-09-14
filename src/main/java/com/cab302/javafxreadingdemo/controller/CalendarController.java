package com.cab302.javafxreadingdemo.controller;


import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import javafx.fxml.FXML;
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
        calendarView.goToday(); // was: showToday()
        calendarView.showDayPage();
    }
}