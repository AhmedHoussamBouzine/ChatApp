package com.chatapp.beans;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Conversation {
    private String id;
    private PrivateKey senderPrivateKey;
    private PublicKey senderPublicKey;
    private PrivateKey receiverPrivateKey;
    private PublicKey receiverPublicKey;
    private SecretKey secretKey ;

    List<Message> messages;


    public Conversation(PrivateKey senderPrivateKey, PublicKey senderPublicKey, PrivateKey receiverPrivateKey, PublicKey receiverPublicKey, SecretKey secretKey) {
        this.id = this.generateConversationId(senderPublicKey,receiverPublicKey);
        this.senderPrivateKey = senderPrivateKey;
        this.senderPublicKey = senderPublicKey;
        this.receiverPrivateKey = receiverPrivateKey;
        this.receiverPublicKey = receiverPublicKey;
        this.secretKey = secretKey;
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

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
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

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public void addMessage(User sender, User receiver, String content) throws Exception {
        // Encrypt the message content before adding to the conversation
        String encryptedContent = Message.encryptMessageContent(content, this.secretKey);
        messages.add(new Message(sender, receiver, encryptedContent));
    }

    public List<Message> getMessages() throws Exception {
        List<Message> decryptedMessages = new ArrayList<>();
        for (Message msg : messages) {
            // Decrypt each message content when retrieving
            String decryptedContent = Message.decryptMessageContent(msg.getContent(), secretKey);
            decryptedMessages.add(new Message(msg.getSender(), msg.getReciever(), decryptedContent));
        }
        return decryptedMessages;
    }
    private  String generateConversationId(PublicKey senderPublicKey, PublicKey receiverPublicKey) {
        String senderKey = Base64.getEncoder().encodeToString(senderPublicKey.getEncoded());
        String receiverKey = Base64.getEncoder().encodeToString(receiverPublicKey.getEncoded());
        return senderKey + "-" + receiverKey;
    }
    public void performKeyExchange() throws Exception {
        secretKey = Conversation.performKeyExchange(senderPrivateKey, receiverPublicKey);
    }
    private static SecretKey performKeyExchange(PrivateKey privateKey, PublicKey publicKey) throws Exception {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey, true);

        byte[] sharedSecret = keyAgreement.generateSecret();
        return new SecretKeySpec(sharedSecret, 0, 16, "AES"); // Adjust key size as needed
    }
}
