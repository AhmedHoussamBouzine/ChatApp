module com.chatapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.chatapp to javafx.fxml;
    exports com.chatapp;
}