package com.chatapp.beans;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class Conversation {
    private long id;
    private String name;
    private PublicKey senderPublicKey;
    private PublicKey receiverPublicKey;
    private List<Message> messages;
    private Date insertedAt;
    private Date updatedAt;

    public Conversation() {
    }

    public Conversation(PublicKey senderPublicKey, PublicKey receiverPublicKey) {
        this.name = generateConversationId(senderPublicKey,receiverPublicKey);
        this.senderPublicKey = senderPublicKey;
        this.receiverPublicKey = receiverPublicKey;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public void setSenderPublicKeyFromString(String publicKeyString) {
        try {
            // Decode the Base64 encoded public key string to get the byte array
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);

            // Create an instance of X509EncodedKeySpec to reconstruct the public key
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

            // Get an instance of the KeyFactory for RSA or your desired algorithm
            // Assuming it's an RSA key
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // Generate the PublicKey from the key specification
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Set the PublicKey in the Etudiant object
            this.senderPublicKey = publicKey;
        } catch (Exception e) {
            // Handle any exceptions that might occur during the conversion
            e.printStackTrace();
            // Optionally throw or log the exception
        }
    }
    public void setReceiverPublicKeyFromString(String publicKeyString) {
        try {
            // Decode the Base64 encoded public key string to get the byte array
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);

            // Create an instance of X509EncodedKeySpec to reconstruct the public key
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

            // Get an instance of the KeyFactory for RSA or your desired algorithm
            // Assuming it's an RSA key
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // Generate the PublicKey from the key specification
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Set the PublicKey in the Etudiant object
            this.receiverPublicKey = publicKey;
        } catch (Exception e) {
            // Handle any exceptions that might occur during the conversion
            e.printStackTrace();
            // Optionally throw or log the exception
        }
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", senderPublicKey=" +  Base64.getEncoder().encodeToString(senderPublicKey.getEncoded())+
                ", receiverPublicKey=" + Base64.getEncoder().encodeToString(receiverPublicKey.getEncoded())+
                ", messages=" + messages +
                ", insertedAt=" + insertedAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
