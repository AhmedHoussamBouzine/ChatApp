package com.chatapp.beans;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class Conversation {
    private String id;
    private PublicKey senderPublicKey;
    private PublicKey receiverPublicKey;
    private List<Message> messages;
    private Date insertedAt;
    private Date updatedAt;

    public Conversation() {
    }

    public Conversation(PublicKey senderPublicKey, PublicKey receiverPublicKey) {
        this.id = generateConversationId(senderPublicKey,receiverPublicKey);
        this.senderPublicKey = senderPublicKey;
        this.receiverPublicKey = receiverPublicKey;
        this.insertedAt = new Date();
        this.updatedAt = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSenderPublicKey(PublicKey senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public void setReceiverPublicKey(PublicKey receiverPublicKey) {
        this.receiverPublicKey = receiverPublicKey;
    }

    public PublicKey getSenderPublicKey() {
        return senderPublicKey;
    }

    public PublicKey getReceiverPublicKey() {
        return receiverPublicKey;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Date getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(Date insertedAt) {
        this.insertedAt = insertedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addMessage(User sender, User receiver, String content) throws Exception {
        // Encrypt the message content before adding to the conversation
        String encryptedContent = Message.encryptMessageContent(content, this.getReceiverPublicKey());
        messages.add(new Message(sender, receiver, encryptedContent));
    }

    public List<Message> getMessages() throws Exception {
        List<Message> decryptedMessages = new ArrayList<>();
        for (Message msg : messages) {
            // Decrypt each message content when retrieving
            //String decryptedContent = Message.decryptMessageContent(msg.getContent(), this.getReceiverPrivateKey());
           //  decryptedMessages.add(new Message(msg.getSender(), msg.getReceiver(), decryptedContent));
        }
        return decryptedMessages;
    }

    private static String generateConversationId(PublicKey senderPublicKey, PublicKey receiverPublicKey) {
        String senderKey = Base64.getEncoder().encodeToString(senderPublicKey.getEncoded());
        String receiverKey = Base64.getEncoder().encodeToString(receiverPublicKey.getEncoded());
        return senderKey + "-" + receiverKey;
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }


}
