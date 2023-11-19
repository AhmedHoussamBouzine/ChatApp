package com.chatapp.dao;


import com.chatapp.beans.Message;

public interface IMessage {
    public Message addMessage(Message message) throws  Exception;
    public Message deleteMessage(Message message) throws Exception;
}
