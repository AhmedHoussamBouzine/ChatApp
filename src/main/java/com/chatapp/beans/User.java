package com.chatapp.beans;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

public class User {
    private long uid ;
    private String username, email, password, telephone;
    private PublicKey publicKey;

    byte[] derivedKey;
    private Date insertedAt;
    private Date updatedAt;

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

    public User(long uid, String username, String email, String password, String telephone) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
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

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", telephone='" + telephone + '\'' +
                ", publicKey=" + Base64.getEncoder().encodeToString(publicKey.getEncoded()) +
                ", insertedAt=" + insertedAt +
                ", updatedAt=" + updatedAt +
                '}';
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
