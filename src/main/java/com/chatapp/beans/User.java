package com.chatapp.beans;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

import static com.chatapp.beans.Conversation.generateKeyPair;

public class User implements Serializable {
    private long uid ;
    private String username, email, password, telephone;
    private PublicKey publicKey;

    byte[] derivedKey;
    private LocalDateTime insertedAt;
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(long uid){
        this.uid = uid ;
    }

    public byte[] getDerivedKey() {
        return derivedKey;
    }

    public void setDerivedKey(byte[] derivedKey) {
        this.derivedKey = derivedKey;
    }

    public User(String username, String email, String password, String telephone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.telephone = telephone;

        KeyPair keyPair = null;
        try {
            keyPair = generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
            String filePath = username+".bin";
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
                outputStream.writeObject(privateKey);
                System.out.println("Object saved to " + filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public long getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getTelephone() {
        return telephone;
    }

    public PublicKey getPublicKey() {
        return publicKey;
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

    @Override
    public String toString() {
        return username;
    }

    public void setPublicKeyFromString(String publicKeyString) {
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

            // Set the PublicKey in the user object
            this.publicKey = publicKey;
        } catch (Exception e) {
            // Handle any exceptions that might occur during the conversion
            e.printStackTrace();
            // Optionally throw or log the exception
        }
    }

}
