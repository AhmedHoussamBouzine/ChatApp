package com.chatapp.presentation.controllers;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;
import com.chatapp.beans.User;
import com.chatapp.business.DefaultServices;
import com.chatapp.business.IServices;
import com.chatapp.presentation.views.MainApplication;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private IServices iServices;
    private Socket socket;
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
    private Button newConversation = new Button("New");
    public static Conversation selectedConversation;
    public static long loggedUserId;
    public static User loggedUser;
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 9090;

    ObservableList<Message> messages = FXCollections.observableArrayList();

    public void addConversation(){
        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL); // Prevents user from accessing other windows
        formStage.setTitle("Start new Conversation");
        VBox formRoot = new VBox(10);
        formRoot.setPadding(new Insets(20));
        ComboBox<User> comboBox = new ComboBox<>();
        try {
            comboBox.getItems().addAll(iServices.getUsers());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Label label = new Label("Choose the receiver:");
        Button submitButton = new Button("Message");

        submitButton.setOnAction(e -> {
            User receiver = comboBox.getSelectionModel().getSelectedItem();
            Conversation conversation = new Conversation(loggedUser,comboBox.getSelectionModel().getSelectedItem());
            try {
                for (Conversation conv : conversations) {
                    if (conv.getReceiver() == conversation.getReceiver() && conv.getSender()== conversation.getSender()) {
                        selectedConversation = conversation;
                        if(selectedConversation.getSender().getUid()==loggedUserId) {
                            conversationReceiver.setText(selectedConversation.getReceiver().getUsername());
                        }else{
                            conversationReceiver.setText(selectedConversation.getSender().getUsername());
                        }
                        getMessages();
                        displayMessages();
                        return;
                    }
                }
                iServices.addConversation(conversation);
                selectedConversation = conversation;
                selectedConversation.setId(iServices.getLastConversation().getId());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            formStage.close();
        });

        formRoot.getChildren().addAll(label, comboBox, submitButton);

        Scene formScene = new Scene(formRoot, 200, 150);
        formStage.setScene(formScene);
        formStage.setOnShown(event -> formStage.centerOnScreen());
        formStage.showAndWait();
        conversations.clear();
        if(selectedConversation.getSender().getUid()==loggedUserId) {
            conversationReceiver.setText(selectedConversation.getReceiver().getUsername());
        }else{
            conversationReceiver.setText(selectedConversation.getSender().getUsername());
        }
        getMessages();
        getConversations();
        displayMessages();
    }

    public void sendMessage() throws Exception {
        socket = new Socket(SERVER_IP, PORT);
        Message message = new Message();
        message.setReceiver(selectedConversation.getReceiver());
        message.setContent(Message.encryptMessageContent(inputMessage.getText(),selectedConversation.getReceiver().getPublicKey()));
        message.setConversation(selectedConversation);
        message.setSender(loggedUser);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(MainApplication.socket.getOutputStream());
        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputMessage.clear();
        iServices.addMessage(message);
        getMessages();

    }

    private void getMessages(){
        messages.clear();
        vboxMessages.setStyle("-fx-background-color: #ECEFF1;");
        vboxMessages.getChildren().clear();
        try {
            selectedConversation.getMessages().clear();
            selectedConversation.setMessages(iServices.getMessagesByConversation(selectedConversation.getId()));

            if(!selectedConversation.getMessages().isEmpty()){
                for (Message message: selectedConversation.getMessages()) {

                    PrivateKey privateKey = null;
                    FileInputStream fileInputStream;
                    String keyFileName = message.getReceiver().getUsername() + ".bin";
                    try {
                        fileInputStream = new FileInputStream(keyFileName);
                        ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
                        Object obj = inputStream.readObject();
                        if (obj instanceof PrivateKey) {
                            privateKey = (PrivateKey) obj;
                        } else {
                            System.out.println("Unexpected object type in file.");
                        }
                        fileInputStream.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    String messageContent = Message.decryptMessageContent(message.getContent(),privateKey) ;
                    String time = message.getInsertedAt().getHour() + ":" + message.getInsertedAt().getMinute();
                    HBox hBox = new HBox();
                    hBox.setPrefWidth(360);
                    hBox.setPrefHeight(20);
                    hBox.setPadding(new Insets(4, 0, 4, 0));
                    Label rowMessage = new Label(messageContent);
                    Label labelTime = new Label(time);
                    rowMessage.setPrefHeight(20);
                    boolean isSentByLoggedUser = (message.getSender().getUid() == loggedUserId);
                    rowMessage.setStyle("-fx-font-size: 11px; -fx-text-fill: #333333; " +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 10px 10px;"+
                            "-fx-margin: 5px 5px;");
                    labelTime.setStyle("-fx-font-size: 7px; -fx-text-fill: #333333; " +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 10px 2px;" +
                            "-fx-text-alignment: center;");
                    if (isSentByLoggedUser) {
                        hBox.setAlignment(Pos.BASELINE_RIGHT);
                        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(14), Insets.EMPTY);
                        Background background = new Background(backgroundFill);
                        rowMessage.setBackground(background);
                        hBox.getChildren().add(rowMessage);
                        hBox.getChildren().add(labelTime);
                    }else{
                        hBox.setAlignment(Pos.TOP_LEFT);
                        BackgroundFill backgroundFill = new BackgroundFill(Color.WHITE, new CornerRadii(14), Insets.EMPTY);
                        Background background = new Background(backgroundFill);
                        rowMessage.setBackground(background);
                        hBox.getChildren().add(labelTime);
                        hBox.getChildren().add(rowMessage);
                    }
                    vboxMessages.getChildren().add(hBox);
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
            Label rowLabel = null;
            if (conversation.getSender().getUid() == loggedUserId){
                rowLabel = new Label(conversation.getReceiver().getUsername());
            }else{
                rowLabel = new Label(conversation.getSender().getUsername());
            }
            rowLabel.setStyle(
                    "-fx-font-size: 11px; " +
                    "-fx-font-weight: bold;"+
                    "-fx-border-width: 1px 0px 1px 0px; " +
                    "-fx-padding: 10px 0px;"+
                    "-fx-width: 100%;" +
                    "-fx-border-color: #ECEFF1;" +
                    "-fx-text-fill: #333333;"
            );
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
                            if(selectedConversation.getSender().getUid()==loggedUserId) {
                                conversationReceiver.setText(selectedConversation.getReceiver().getUsername());
                            }else{
                                conversationReceiver.setText(selectedConversation.getSender().getUsername());
                            }
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
            conversations.setAll(iServices.getConversations());
            selectedConversation  = (Conversation) (iServices.getConversations()).get(0);
            loggedUser = iServices.getUser(loggedUserId);
            messages.setAll(selectedConversation.getMessages());
            getConversations();
            displayMessages();
            if(selectedConversation.getSender().getUid()==loggedUserId) {
                conversationReceiver.setText(selectedConversation.getReceiver().getUsername());
            }else{
                conversationReceiver.setText(selectedConversation.getSender().getUsername());
            }
            startMessageReceiver();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void startMessageReceiver() {
        Runnable r = () -> {
            while (true) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(MainApplication.socket.getInputStream());
                    Message receivedMsg = (Message) objectInputStream.readObject();
                    Platform.runLater(this::getMessages);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        new Thread(r).start();
    }

    public IServices getiServices() {
        return iServices;
    }

    public void setiServices(IServices iServices) {
        this.iServices = iServices;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setConversations(ObservableList<Conversation> conversations) {
        this.conversations = conversations;
    }

    public VBox getVboxConversations() {
        return vboxConversations;
    }

    public void setVboxConversations(VBox vboxConversations) {
        this.vboxConversations = vboxConversations;
    }

    public VBox getVboxMessages() {
        return vboxMessages;
    }

    public void setVboxMessages(VBox vboxMessages) {
        this.vboxMessages = vboxMessages;
    }

    public Button getSendButton() {
        return sendButton;
    }

    public void setSendButton(Button sendButton) {
        this.sendButton = sendButton;
    }

    public TextField getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(TextField inputMessage) {
        this.inputMessage = inputMessage;
    }

    public Label getConversationReceiver() {
        return conversationReceiver;
    }

    public void setConversationReceiver(Label conversationReceiver) {
        this.conversationReceiver = conversationReceiver;
    }

    public ScrollPane getDivMessages() {
        return divMessages;
    }

    public void setDivMessages(ScrollPane divMessages) {
        this.divMessages = divMessages;
    }

    public ScrollPane getSidebar() {
        return sidebar;
    }

    public void setSidebar(ScrollPane sidebar) {
        this.sidebar = sidebar;
    }

    public Button getNewConversation() {
        return newConversation;
    }

    public void setNewConversation(Button newConversation) {
        this.newConversation = newConversation;
    }

    public Conversation getSelectedConversation() {
        return selectedConversation;
    }

    public void setSelectedConversation(Conversation selectedConversation) {
        this.selectedConversation = selectedConversation;
    }

    public long getLoggedUserId() {
        return loggedUserId;
    }

    public void setLoggedUserId(long loggedUserId) {
        this.loggedUserId = loggedUserId;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setMessages(ObservableList<Message> messages) {
        this.messages = messages;
    }
}
