package com.chatapp.presentation.views;

import com.chatapp.beans.User;
import com.chatapp.presentation.controllers.LoginController;
import com.chatapp.presentation.controllers.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class MainApplication extends Application {
    public static Socket socket ;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Platform.setImplicitExit(true);
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/chatapp/fxml/Login.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login to ChatApp");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try {
            socket = new Socket("localhost",9090);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        launch(args);
    }
}