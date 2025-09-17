module com.cab302.javafxreadingdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires java.sql;
    requires jbcrypt;

    opens com.cab302.javafxreadingdemo to javafx.fxml;
    opens com.cab302.javafxreadingdemo.controller to javafx.fxml;
    opens com.cab302.javafxreadingdemo.model to javafx.base;

    exports com.cab302.javafxreadingdemo;
    exports com.cab302.javafxreadingdemo.controller;
    exports com.cab302.javafxreadingdemo.controller.Flashcards;
    opens com.cab302.javafxreadingdemo.controller.Flashcards to javafx.fxml;
}
