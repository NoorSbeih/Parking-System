module com.example.parking {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.parking to javafx.fxml;
    exports com.example.parking;
}