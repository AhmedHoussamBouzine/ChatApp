package com.chatapp.beans;

import javax.crypto.Cipher;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

public class Message implements Serializable {
    private long id;
    private User sender, receiver;
    private String content;
    private LocalDateTime insertedAt;
    private LocalDateTime updatedAt;

    private Conversation conversation;

    public Message() {
    }

    public Message(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;

    }
    public LocalDateTime getInsertedAt() {
        return insertedAt;
    }
    public void setInsertedAt(LocalDateTime insertedAt) {
        this.insertedAt = insertedAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }



    public static String encryptMessageContent(String content, PublicKey receiverPublicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, receiverPublicKey);
        byte[] encryptedBytes = cipher.doFinal(content.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptMessageContent(String encryptedContent, PrivateKey receiverPrivateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, receiverPrivateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedContent));
        return new String(decryptedBytes);
    }


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender=" + sender.getUsername() +
                ", receiver=" + receiver.getUsername() +
                ", content='" + content + '\'' +
                ", insertedAt=" + insertedAt +
                ", updatedAt=" + updatedAt +
                ", conversation=" + conversation +
                '}';
    }
}
