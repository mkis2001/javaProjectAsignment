module hr.java {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;


    opens hr.java.musicshop.controller to javafx.fxml;
    exports hr.java.musicshop.controller;
}