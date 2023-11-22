package com.chatapp.beans;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class Conversation {
    private String id;
    private PrivateKey senderPrivateKey;
    private PublicKey senderPublicKey;
    private PrivateKey receiverPrivateKey;
    private PublicKey receiverPublicKey;

    private List<Message> messages;

    private Date insertedAt;
    private Date updatedAt;

    public Conversation() {
    }

    public Conversation(PrivateKey senderPrivateKey, PublicKey senderPublicKey, PrivateKey receiverPrivateKey, PublicKey receiverPublicKey) {
        this.id = generateConversationId(senderPublicKey,receiverPublicKey);
        this.senderPrivateKey = senderPrivateKey;
        this.senderPublicKey = senderPublicKey;
        this.receiverPrivateKey = receiverPrivateKey;
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

    public void setSenderPrivateKey(PrivateKey senderPrivateKey) {
        this.senderPrivateKey = senderPrivateKey;
    }

    public void setSenderPublicKey(PublicKey senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public void setReceiverPrivateKey(PrivateKey receiverPrivateKey) {
        this.receiverPrivateKey = receiverPrivateKey;
    }

    public void setReceiverPublicKey(PublicKey receiverPublicKey) {
        this.receiverPublicKey = receiverPublicKey;
    }

    public PrivateKey getSenderPrivateKey() {
        return senderPrivateKey;
    }

    public PublicKey getSenderPublicKey() {
        return senderPublicKey;
    }

    public PrivateKey getReceiverPrivateKey() {
        return receiverPrivateKey;
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
            String decryptedContent = Message.decryptMessageContent(msg.getContent(), this.getReceiverPrivateKey());
            decryptedMessages.add(new Message(msg.getSender(), msg.getReceiver(), decryptedContent));
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
        keyPairGenerator.initialize(2048); // Adjust key size as needed
        return keyPairGenerator.generateKeyPair();
    }

}
