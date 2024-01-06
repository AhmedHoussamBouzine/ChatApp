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
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.util.Objects;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    User loggedUser;
    private IServices iServices;

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
            MainController.loggedUser = loggedUser;
            PrintWriter printWriter = new PrintWriter(input, true);
            printWriter.println(loggedUser.getUsername());

            if (loggedUser == null) {
                return false;
            } else {
//                if (Objects.equals(loggedUser.getUsername(), username) && Objects.equals(loggedUser.getPassword(), BCrypt.hashpw(password, BCrypt.gensalt()))) {
//                    return true;
//                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private void openMainInterface(User loggedUser) {
        try {
            FXMLLoader loader =  new FXMLLoader(MainApplication.class.getResource("/com/chatapp/fxml/Main.fxml"));
            Parent root = loader.load();
            MainController mainController = loader.getController();

            mainController.setLoggedUser(loggedUser);
            mainController.setLoggedUserId(loggedUser.getUid());

            // Create a new stage for the main interface
            Stage mainStage = new Stage();
            mainStage.setScene(new Scene(root));
            mainStage.setTitle("ChatApp");
            mainStage.show();

            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
