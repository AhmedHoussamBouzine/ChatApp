package com.chatapp.dao;

import com.chatapp.beans.Message;

import java.util.List;

public interface IMessage {
    public Message addMessage(Message message) throws Exception ;
    public Message deleteMessage(Message message) throws Exception;
    public Message updateMessage(Message message) throws Exception;
    public Message getMessage(long id) throws Exception;
    public List<Message> getMessages() throws Exception;
}
