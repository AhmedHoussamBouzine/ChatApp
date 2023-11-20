package com.chatapp.beans;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class User {
    private long uid ;
    private String username, email, password, telephone;
    PublicKey publicKey;
    PrivateKey privateKey ;

    private Date insertedAt;
    private Date updatedAt;


    public User() {
    }

    public User(long uid, String username, String email, String password, String telephone) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.insertedAt = new Date();
        this.updatedAt = new Date();
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

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
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

    public PrivateKey getPrivateKey() {
        return privateKey;
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
}
