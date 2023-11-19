package com.chatapp.beans;

public class Conversation {
    private String senderPrivateKey, senderPublicKey, receiverPrivateKey, receiverPublicKey, secretKey ;

    public Conversation(String senderPrivateKey, String senderPublicKey, String receiverPrivateKey, String receiverPublicKey, String secretKey) {
        this.senderPrivateKey = senderPrivateKey;
        this.senderPublicKey = senderPublicKey;
        this.receiverPrivateKey = receiverPrivateKey;
        this.receiverPublicKey = receiverPublicKey;
        this.secretKey = secretKey;
    }

    public String getSenderPrivateKey() {
        return senderPrivateKey;
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public String getReceiverPrivateKey() {
        return receiverPrivateKey;
    }

    public String getReceiverPublicKey() {
        return receiverPublicKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
