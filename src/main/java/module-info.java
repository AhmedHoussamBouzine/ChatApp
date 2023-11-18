module com.chatapp {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.chatapp.presentation.controllers to javafx.fxml;
    exports com.chatapp.presentation.controllers;

    opens com.chatapp.presentation.views to javafx.fxml;
    exports com.chatapp.presentation.views;
}
