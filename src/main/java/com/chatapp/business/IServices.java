package com.chatapp.business;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;
import com.chatapp.beans.User;

import java.util.List;

public interface IServices {

    // user
    public User addUser(User user) throws  Exception;
    public User deleteUser(User user) throws Exception;
    public User updateUser(User user) throws Exception;
    public List<User> getUsers() throws Exception;

    // Message
    public Message addMessage(Message message) throws  Exception;
    public Message deleteMessage(Message message) throws Exception;


    // Conversation
    public Conversation addConversation(Conversation conversation) throws  Exception;
    public Conversation deleteConversation(Conversation conversation) throws Exception;
}
