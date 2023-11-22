package com.chatapp.business;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;
import com.chatapp.beans.User;

import java.util.List;

public interface IServices {
    // conversation
    public Conversation addConversation(Conversation conversation) throws Exception ;
    public boolean deleteConversation(long id) throws Exception;
    public boolean updateConversation(Conversation conversation) throws Exception;
    public Conversation getConversation(long id) throws Exception;
    public List<Conversation> getConversations() throws Exception;


    //user
    public User addUser(User user) throws Exception ;
    public boolean deleteUser(long id) throws Exception;
    public boolean updateUser(User user) throws Exception;
    public User getUser(long id) throws Exception;
    public List<User> getUsers() throws Exception;

    // message
    public Message addMessage(Message message) throws Exception ;
    public boolean deleteMessage(long id) throws Exception;
    public boolean updateMessage(Message message) throws Exception;
    public Message getMessage(long id) throws Exception;
    public List<Message> getMessages() throws Exception;
}
