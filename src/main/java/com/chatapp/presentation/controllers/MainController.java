package com.chatapp.presentation.controllers;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;
import com.chatapp.business.DefaultServices;
import com.chatapp.business.IServices;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    IServices iServices;
    List<Conversation> conversations = new ArrayList<Conversation>();
    @FXML
    private VBox vboxConversations ;
    @FXML
    private VBox vboxMessages ;
    @FXML
    private Label conversationReceiver;
    Button newConversation = new Button("New");
    long loggedUserId = 1;

    Conversation selectedConversation;

    List<Message> messages = new ArrayList<>();

    private void getMessages(){
        vboxMessages.setStyle(" -fx-background-color: #ECEFF1; \n" +
                "    -fx-width: 100%;\n");
        try {

            for (Message message: selectedConversation.getMessages()) {
                String messageContent = message.getContent();
                Label rowMessage = new Label(messageContent);
                rowMessage.setPrefWidth(360);
                rowMessage.setPrefHeight(20);
                rowMessage.setStyle("-fx-font-size: 11px; -fx-text-fill: #333333; " +
                        "-fx-font-weight: bold; -fx-alignment: center-left; " +
                        "-fx-padding: 10px 0px;" +
                        "-fx-width: 100%;");
                System.out.println(selectedConversation.toString());
                boolean isSentByLoggedUser = (message.getSender().getUid() == loggedUserId);
                if (isSentByLoggedUser) {
                    rowMessage.setStyle("-fx-font-size: 11px; -fx-text-fill: #333333; " +
                    "-fx-font-weight: bold; -fx-alignment: center-right; " +
                            "-fx-padding: 10px 0px;" +
                            "-fx-width: 100%;");
                }
                vboxMessages.getChildren().add(rowMessage);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iServices = new DefaultServices();
        try {
            conversations = iServices.getConversations();
            selectedConversation  = iServices.getConversation(1);

            for (Conversation conversation: conversations) {
                String conversationName = conversation.getName();
                Label rowLabel = new Label(conversationName);

                rowLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #333333; " +
                        "-fx-font-weight: bold; -fx-alignment: center; " +
                        "-fx-border-width: 1px 0px 1px 0px; " +
                        "-fx-border-color: #CCCCCC; " +
                        "-fx-padding: 10px 0px;");

                System.out.println(conversation.toString());
                rowLabel.setUserData(conversation.getId());

                vboxConversations.getChildren().add(rowLabel);
            }
            conversationReceiver.setText(selectedConversation.getReceiver().getUsername());
            vboxConversations.getChildren().add(newConversation);

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
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
