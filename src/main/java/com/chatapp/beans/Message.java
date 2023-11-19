package com.chatapp.beans;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

public class Message {
    private User sender, reciever ;
    private String content;

    public Message(User sender, User receiver,String content) {
        this.sender = sender;
        this.reciever = receiver;
        this.content = content;
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

    public User getReciever() {
        return reciever;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public static String encryptMessageContent(String content, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(content.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptMessageContent(String encryptedContent, SecretKey secretKey) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedContent);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
