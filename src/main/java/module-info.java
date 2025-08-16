module com.cab302.javafxreadingdemo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.cab302.javafxreadingdemo to javafx.fxml;
    exports com.cab302.javafxreadingdemo;
}