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
    public static ObjectOutputStream objectOutputStream ;
    private static ObjectInputStream objectInputStream;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Platform.setImplicitExit(true);
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/chatapp/fxml/Login.fxml"));
        FXMLLoader mainControllerloader = new FXMLLoader(MainApplication.class.getResource("/com/chatapp/fxml/Main.fxml"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(MainApplication.class.getResource("/com/chatapp/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
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