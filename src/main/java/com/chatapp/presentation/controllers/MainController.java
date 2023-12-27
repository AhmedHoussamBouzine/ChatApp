package com.chatapp.presentation.controllers;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;
import com.chatapp.beans.User;
import com.chatapp.business.DefaultServices;
import com.chatapp.business.IServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.security.PrivateKey;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    IServices iServices;
    Socket socket;
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
    long loggedUserId = 6;
    User loggedUser;
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 9090;

    ObservableList<Message> messages = FXCollections.observableArrayList();

    public void addConversation(){
        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL); // Block interactions with other windows
        formStage.setTitle("Create new Conversation");

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
            Conversation conversation = new Conversation(loggedUser,comboBox.getSelectionModel().getSelectedItem());
            try {
                for (Conversation conv : conversations) {
                    if (conv.getReceiver() == conversation.getReceiver() && conv.getSender()== conversation.getSender()) {
                        selectedConversation = conversation;
                        conversationReceiver.setText(selectedConversation.getReceiver().getUsername());
                        getMessages();
                        displayMessages();
                        return;
                    }
                }
                iServices.addConversation(conversation);
                selectedConversation = conversation;
                selectedConversation.setId(iServices.getLastConversation().getId());
                System.out.println(selectedConversation);
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
        socket = new Socket(SERVER_IP, PORT);
        Message message = new Message();
        message.setContent(Message.encryptMessageContent(inputMessage.getText(),selectedConversation.getReceiver().getPublicKey()));
        message.setConversation(selectedConversation);
        message.setSender(loggedUser);
        message.setReceiver(selectedConversation.getReceiver());
        System.out.println(message);
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        try {
            outputStream.writeObject(message);
            outputStream.flush();
            Object receivedObject = inputStream.readObject();
            if (receivedObject instanceof Message) {
                Message confirmationMessage = (Message) receivedObject;
                if (confirmationMessage.getReceiver().getUid() == loggedUserId){
                    System.out.println("Confirmation received from server: " + confirmationMessage.getContent());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {

            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
            if(!selectedConversation.getMessages().isEmpty()){
                for (Message message: selectedConversation.getMessages()) {
                    PrivateKey privateKey = null;
                    try {
                        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(message.getReceiver().getUsername()+".bin"));
                        Object obj = inputStream.readObject();

                        if (obj instanceof PrivateKey) {
                            privateKey = (PrivateKey) obj;
                        } else {
                            System.out.println("Unexpected object type in file.");
                        }
                    }catch (Exception e){

                    }
                    String messageContent = Message.decryptMessageContent(message.getContent(),privateKey);
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
    private void handleServerMessages() {
        try {
            Socket socket = new Socket(SERVER_IP, PORT);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Message receivedMessage = (Message) inputStream.readObject();

                System.out.println("Received message: " + receivedMessage.getContent());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iServices = new DefaultServices();
        try {
            loggedUser = iServices.getUser(loggedUserId);
            conversations.setAll(iServices.getConversations());
            selectedConversation  = iServices.getConversation(21);
            messages.setAll(selectedConversation.getMessages());
            getConversations();
            displayMessages();
            conversationReceiver.setText(selectedConversation.getReceiver().getUsername());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
