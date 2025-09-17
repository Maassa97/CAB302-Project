module com.cab302.javafxreadingdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires java.sql;
    requires jbcrypt;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires jdk.httpserver;

    opens com.cab302.javafxreadingdemo.controller to javafx.fxml;

    // CalendarFX (official JPMS module)
    requires com.calendarfx.view;
    // ControlsFX (CalendarFX dependent)
    requires org.controlsfx.controls;

    opens com.cab302.javafxreadingdemo to javafx.fxml;
    opens com.cab302.javafxreadingdemo.model to javafx.base;

    exports com.cab302.javafxreadingdemo;
    exports com.cab302.javafxreadingdemo.controller;
}


