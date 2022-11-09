module com.example.carrentalsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.h2database;
    requires json.simple;


    opens carrentalmanagementsystem to javafx.fxml;
    exports carrentalmanagementsystem;
}