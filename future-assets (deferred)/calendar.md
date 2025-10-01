<h1> Future Calendar Assets to be Implemented </h1>


CalendarController.java

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

calendar-view.fxml:

<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.control.Button?>
<?import com.calendarfx.view.CalendarView?>

<BorderPane xmlns="http://javafx.com/javafx/21"
xmlns:fx="http://javafx.com/fxml/1"
fx:controller="com.cab302.javafxreadingdemo.controller.CalendarController">

    <!-- Top: Back Button -->
    <top>
        <Button text="Home" onAction="#onBackToHome" />
    </top>

    <!-- Center: CalendarView -->
    <center>
        <CalendarView fx:id="calendarView" />
    </center>
</BorderPane>


        <!-- CalendarFX implementation-->
        <dependency>
            <groupId>com.calendarfx</groupId>
            <artifactId>view</artifactId>
            <version>11.12.6</version>
        </dependency>
        <!-- ControlsFX required in CalendarFX implementation -->
        <dependency>
            <groupId>org.controlsfx</groupId>
            <artifactId>controlsfx</artifactId>
            <version>11.2.2</version>
        </dependency>


HomeController:

    // Handles calendar opening
    @FXML
    private void onOpenCalendar(ActionEvent e) {
        try {
            Parent calendarRoot = FXMLLoader.load(
                    getClass().getResource("/com/cab302/javafxreadingdemo/calendar-view.fxml")
            );
            ((Node) e.getSource()).getScene().setRoot(calendarRoot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

home-view.fxml:

            <VBox prefHeight="200.0" prefWidth="100.0" VBox.vgrow="ALWAYS">
               <children>
                  <HBox alignment="CENTER_LEFT" minHeight="50.0" prefHeight="100.0" prefWidth="200.0" VBox.vgrow="ALWAYS">
                     <children>
                          <Button minHeight="80.0" minWidth="300.0" onAction="#onOpenCalendar" prefHeight="40.0" text="Calendar" HBox.hgrow="SOMETIMES">
                           <font>
                              <Font size="30.0" />
                           </font>
                           <HBox.margin>
                              <Insets bottom="10.0" top="10.0" />
                           </HBox.margin></Button>
                        <Label minWidth="400.0" text="All in one calendar to help you stay on top of your assignments " wrapText="true">
                           <padding>
                              <Insets left="20.0" />
                           </padding>
                           <font>
                              <Font size="18.0" />
                           </font>
                        </Label>
                     </children>
                  </HBox>
               </children>
            </VBox>

    // CalendarFX (official JPMS module)
    requires com.calendarfx.view;
    // ControlsFX (CalendarFX dependent)
    requires org.controlsfx.controls;