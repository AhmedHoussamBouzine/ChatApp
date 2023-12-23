package com.chatapp.presentation.controllers;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;
import com.chatapp.beans.User;
import com.chatapp.business.DefaultServices;
import com.chatapp.business.IServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    IServices iServices;
    ObservableList<Conversation> conversations = FXCollections.observableArrayList();
    @FXML
    private VBox vboxConversations ;
    @FXML
    private VBox vboxMessages ;
    @FXML
    private Button sendButton;
    @FXML
    private TextField inputMessage;
    @FXML
    private Label conversationReceiver;
    @FXML
    private ScrollPane divMessages;
    @FXML
    private ScrollPane sidebar;
    Button newConversation = new Button("New");
    Conversation selectedConversation;
    long loggedUserId = 1;

    User loggedUser;
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 9090;

    ObservableList<Message> messages = FXCollections.observableArrayList();

    public void addConversation(){
        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL); // Block interactions with other windows
        formStage.setTitle("Add Data Form");

        VBox formRoot = new VBox(10);
        formRoot.setPadding(new Insets(20));

        ComboBox<User> comboBox = new ComboBox<>();
        try {
            comboBox.getItems().addAll(iServices.getUsers());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Label label = new Label("Choose a receiver:");
        Button submitButton = new Button("Message");

        submitButton.setOnAction(e -> {
            User receiver = comboBox.getSelectionModel().getSelectedItem();
            Conversation conversation = new Conversation(comboBox.getSelectionModel().getSelectedItem(),comboBox.getSelectionModel().getSelectedItem());
            selectedConversation = conversation;
            try {
                iServices.addConversation(conversation);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Added to database: " + receiver.getUsername());
            formStage.close();
        });

        formRoot.getChildren().addAll(label, comboBox, submitButton);

        Scene formScene = new Scene(formRoot, 300, 200);
        formStage.setScene(formScene);
        formStage.setOnShown(event -> formStage.centerOnScreen());
        formStage.showAndWait();
        conversations.clear();
        conversationReceiver.setText(selectedConversation.getReceiver().getUsername());
        getMessages();
        getMessages();
        getConversations();
        displayMessages();
    }

    public void sendMessage() throws Exception {
        Message message = new Message();
        message.setContent(inputMessage.getText());
        message.setConversation(selectedConversation);
        message.setSender(loggedUser);
        message.setReceiver(loggedUser);
        System.out.println(message);
        Socket socket = new Socket(SERVER_IP, PORT);
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);


        User user = loggedUser;
        output.println(loggedUser.getUsername()); // Send username to server

        new Thread(() -> {
            try {
                BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String messageToSend = inputMessage.getText();

                while ((messageToSend = serverInput.readLine()) != null) {
                    System.out.println(messageToSend);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        String input = inputMessage.getText(); // Get message from your JavaFX text field
        output.println(input); // Send the message to the server

        iServices.addMessage(message);
        inputMessage.clear();
        messages.clear();
        getMessages();
    }

    private void getMessages(){
        vboxMessages.setStyle(" -fx-background-color: #ECEFF1; \n" +
                "    -fx-width: 100%;\n");
        vboxMessages.getChildren().clear();
        try {
            System.out.println(selectedConversation.toString());
            selectedConversation.getMessages().clear();
            selectedConversation.setMessages(iServices.getMessagesByConversation(selectedConversation.getId()));
            for (Message message: selectedConversation.getMessages()) {
                String messageContent = message.getContent();
                Label rowMessage = new Label(messageContent);
                rowMessage.setPrefWidth(360);
                rowMessage.setPrefHeight(20);
                rowMessage.setStyle("-fx-font-size: 11px; -fx-text-fill: #333333; " +
                        "-fx-font-weight: bold; -fx-alignment: center-left; " +
                        "-fx-padding: 10px 0px;" +
                        "-fx-width: 100%;");
                boolean isSentByLoggedUser = (message.getSender().getUid() == loggedUserId);
                if (isSentByLoggedUser) {
                    rowMessage.setStyle("-fx-font-size: 11px; -fx-text-fill: #333333; " +
                    "-fx-font-weight: bold; -fx-alignment: center-right; " +
                            "-fx-padding: 10px 0px;" +
                            "-fx-width: 100%;");
                }
                vboxMessages.getChildren().add(rowMessage);
                vboxMessages.heightProperty().addListener((observable, oldValue, newValue) -> {
                    divMessages.setVvalue(1.0); // Scroll to the bottom
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void getConversations(){
        vboxConversations.setStyle("-fx-width: 100%;\n");
        vboxConversations.setAlignment(Pos.CENTER);
        vboxConversations.getChildren().clear();
        try {
            conversations.setAll(iServices.getConversations());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (Conversation conversation: conversations) {
            String conversationName = conversation.getName();
            Label rowLabel = new Label(conversation.getReceiver().getUsername());

            rowLabel.setStyle(
                    "-fx-font-size: 11px; " +
                    "-fx-font-weight: bold;"+
                    "-fx-border-width: 1px 0px 1px 0px; " +
                    "-fx-padding: 10px 0px;"+
                    "-fx-width: 100%;"
            );
            System.out.println(conversation.toString());
            rowLabel.setUserData(conversation.getId());

            vboxConversations.getChildren().add(rowLabel);
        }
        vboxConversations.heightProperty().addListener((observable, oldValue, newValue) -> {
            sidebar.setVvalue(1.0); // Scroll to the bottom
        });
        newConversation.setId("newConversation");
        vboxConversations.getChildren().add(newConversation);

    }
    public void displayMessages(){
        for (Node node : vboxConversations.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                label.setOnMouseClicked(event -> {

                    long clickedConversationId = (long) label.getUserData();

                    for (Conversation conv : conversations) {
                        if (conv.getId() == clickedConversationId) {
                            selectedConversation = conv;
                            conversationReceiver.setText(selectedConversation.getReceiver().getUsername());
                            vboxMessages.getChildren().clear();
                            getMessages();
                            break;
                        }
                    }
                });
            }
            vboxMessages.getChildren().clear();
            getMessages();
            newConversation.setOnAction(e -> addConversation());
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iServices = new DefaultServices();
        try {
            loggedUser = iServices.getUser(loggedUserId);
            conversations.setAll(iServices.getConversations());
            selectedConversation  = iServices.getConversation(3);
            messages.setAll(selectedConversation.getMessages());
            getConversations();
            displayMessages();
            conversationReceiver.setText(selectedConversation.getReceiver().getUsername());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
