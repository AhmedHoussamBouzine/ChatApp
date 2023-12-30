package com.chatapp.presentation.controllers;

import com.chatapp.beans.User;
import com.chatapp.business.DefaultServices;
import com.chatapp.business.IServices;
import com.chatapp.presentation.views.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    User loggedUser;
    private IServices iServices;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public void initStreams(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.objectInputStream = inputStream;
        this.objectOutputStream = outputStream;
    }

    public LoginController() {
        iServices = new DefaultServices();
    }

    @FXML
    private void loginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean isAuthenticated = authenticate(username, password);

        if (isAuthenticated) {
            openMainInterface(loggedUser);
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private boolean authenticate(String username, String password) {
        try {
            loggedUser = iServices.getUserByUsername(username);
            OutputStreamWriter input = new OutputStreamWriter(MainApplication.socket.getOutputStream());
            MainController.loggedUserId = loggedUser.getUid();
            PrintWriter printWriter = new PrintWriter(input, true);
            printWriter.println(loggedUser.getUsername());

            if (loggedUser == null) {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private void openMainInterface(User loggedUser) {
        try {
            // Load the MainInterface.fxml file
            FXMLLoader loader =  new FXMLLoader(MainApplication.class.getResource("/com/chatapp/fxml/Main.fxml"));
            Parent root = loader.load();
            MainController mainController = loader.getController();

            // Set the logged-in user in the MainController
            mainController.setLoggedUser(loggedUser);
            mainController.setLoggedUserId(loggedUser.getUid());

            // Create a new stage for the main interface
            Stage mainStage = new Stage();
            mainStage.setScene(new Scene(root));
            mainStage.setTitle("Main Interface");
            mainStage.show();

            // Close the login window (if needed)
            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}