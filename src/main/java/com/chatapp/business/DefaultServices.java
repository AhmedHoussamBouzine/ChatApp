package com.chatapp.business;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;
import com.chatapp.beans.User;
import com.chatapp.dao.*;

import java.util.List;

public class DefaultServices implements IServices {

    private static DefaultServices instance = null;
    private IMessage iMessage;
    private IConversation iConversation;
    private IUser iUser;


    private DefaultServices() {
        iMessage = new MessageDaoJDBC();
        iConversation = new ConversationDaoJDBC();
        iUser = new UserDaoJDBC();
    }
    public static DefaultServices getInstance(){

        if(instance == null)
            instance = new DefaultServices();

        return instance;

    }

    @Override
    public Conversation addConversation(Conversation conversation) throws Exception {
        return iConversation.addConversation(conversation);
    }

    @Override
    public boolean deleteConversation(String id) throws Exception {
        return iConversation.deleteConversation(id);
    }

    @Override
    public boolean updateConversation(Conversation conversation) throws Exception {
        return iConversation.updateConversation(conversation);
    }

    @Override
    public Conversation getConversation(String id) throws Exception {
        return iConversation.getConversation(id);
    }

    @Override
    public List<Conversation> getConversations() throws Exception {
        return iConversation.getConversations();
    }

    @Override
    public User addUser(User user) throws Exception {
        return iUser.addUser(user);
    }

    @Override
    public boolean deleteUser(long id) throws Exception {
        return iUser.deleteUser(id);
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        return iUser.updateUser(user);
    }

    @Override
    public User getUser(long id) throws Exception {
        return iUser.getUser(id);
    }

    @Override
    public List<User> getUsers() throws Exception {
        return iUser.getUsers();
    }

    @Override
    public Message addMessage(Message message) throws Exception {
        return iMessage.addMessage(message);
    }

    @Override
    public boolean deleteMessage(long id) throws Exception {
        return iMessage.deleteMessage(id);
    }

    @Override
    public boolean updateMessage(Message message) throws Exception {
        return iMessage.updateMessage(message);
    }

    @Override
    public Message getMessage(long id) throws Exception {
        return iMessage.getMessage(id);
    }
    @Override
    public List<Message> getMessages() throws Exception {
        return iMessage.getMessages();
    }
}
