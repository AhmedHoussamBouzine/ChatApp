package com.chatapp.beans;

public class Message {
    private User sender, reciever ;

    public Message(User sender, User receiver) {
        this.sender = sender;
        this.reciever = receiver;
    }

    public User getSender() {
        return sender;
    }

    public User getReciever() {
        return reciever;
    }
}
