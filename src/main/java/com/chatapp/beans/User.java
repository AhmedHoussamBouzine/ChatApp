package com.chatapp.beans;

public class User {
    private long uid ;
    private String username, email, password, telephone, publicKey, privateKey ;

    public User() {
    }

    public User(long uid, String username, String email, String password, String telephone, String publicKey, String privateKey) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
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

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(String privateKey) {
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

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

}
