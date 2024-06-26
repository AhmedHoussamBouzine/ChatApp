package com.chatapp.beans;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class Conversation implements Serializable {
    private long id;
    private String name;
    private List<Message> messages;
    private User sender, receiver;
    private LocalDateTime insertedAt;
    private LocalDateTime updatedAt;


    public Conversation() {
    }

    public Conversation(User sender, User receiver) {
        this.name = generateConversationId(sender.getPublicKey(),receiver.getPublicKey());
        this.receiver = receiver;
        this.sender = sender;
        this.messages = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMessage(User sender, User receiver, String content) throws Exception {
        // Encrypt the message content before adding to the conversation
        String encryptedContent = Message.encryptMessageContent(content, this.receiver.getPublicKey());
        messages.add(new Message(sender, receiver, encryptedContent));
    }

    public List<Message> getMessages() throws Exception {
        List<Message> decryptedMessages = new ArrayList<>();
        PrivateKey privateKey = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(receiver.getUsername()+".bin"))) {
            // Read the object from the file
            Object obj = inputStream.readObject();

            if (obj instanceof PrivateKey) {
                privateKey = (PrivateKey) obj;
            } else {
                System.out.println("Unexpected object type in file.");
            }
        for (Message msg : messages) {
           String decryptedContent = Message.decryptMessageContent(msg.getContent(), privateKey);
             decryptedMessages.add(new Message(msg.getSender(), msg.getReceiver(), decryptedContent));
        }
        }
        return messages;
    }

    private static String generateConversationId(PublicKey senderPublicKey, PublicKey receiverPublicKey) {
        String senderKey = Base64.getEncoder().encodeToString(senderPublicKey.getEncoded());
        String receiverKey = Base64.getEncoder().encodeToString(receiverPublicKey.getEncoded());
        return senderKey + "-" + receiverKey;
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

//    public void setSenderPublicKeyFromString(String publicKeyString) {
//        try {
//            // Decode the Base64 encoded public key string to get the byte array
//            byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
//
//            // Create an instance of X509EncodedKeySpec to reconstruct the public key
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
//
//            // Get an instance of the KeyFactory for RSA or your desired algorithm
//            // Assuming it's an RSA key
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//            // Generate the PublicKey from the key specification
//            PublicKey publicKey = keyFactory.generatePublic(keySpec);
//
//
//            sender.getPublicKey() = publicKey;
//        } catch (Exception e) {
//            // Handle any exceptions that might occur during the conversion
//            e.printStackTrace();
//            // Optionally throw or log the exception
//        }
//    }
//    public void setReceiverPublicKeyFromString(String publicKeyString) {
//        try {
//            // Decode the Base64 encoded public key string to get the byte array
//            byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
//
//            // Create an instance of X509EncodedKeySpec to reconstruct the public key
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
//
//            // Get an instance of the KeyFactory for RSA or your desired algorithm
//            // Assuming it's an RSA key
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//            // Generate the PublicKey from the key specification
//            PublicKey publicKey = keyFactory.generatePublic(keySpec);
//
//            // Set the PublicKey in the Etudiant object
//            this.receiverPublicKey = publicKey;
//        } catch (Exception e) {
//            // Handle any exceptions that might occur during the conversion
//            e.printStackTrace();
//            // Optionally throw or log the exception
//        }
//    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", messages=" + messages +
                ", sender=" + sender.getUsername() +
                ", receiver=" + receiver.getUsername() +
                ", insertedAt=" + insertedAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
