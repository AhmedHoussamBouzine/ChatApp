package com.chatapp.presentation.controllers;

import com.chatapp.beans.Conversation;
import com.chatapp.business.DefaultServices;
import com.chatapp.business.IServices;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iServices = new DefaultServices();
        try {
            conversations = iServices.getConversations();

            for (Conversation conversation: conversations) {
                String conversationName = conversation.getName();
                Label rowLabel = new Label(conversationName);
                rowLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #333333; " +
                        "-fx-font-weight: bold; -fx-alignment: center; " +
                        "-fx-border-width: 1px 0px 1px 0px; " +
                        "-fx-border-color: #CCCCCC; " +
                        "-fx-padding: 10px 0px;");
                System.out.println(conversation.toString());
                vboxConversations.getChildren().add(rowLabel);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
