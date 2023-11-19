package com.chatapp.business;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;
import com.chatapp.beans.User;
import com.chatapp.dao.*;

import java.util.List;

public class DefaultServices implements IServices {

    private static DefaultServices instance = null;
    private IMessage iMessage ;
    private IUser iUser;
    private IConversation iConversation;

    private DefaultServices() {
        iMessage = new MessageDaoJDBC();
        iUser = new UserDaoJDBC();
        iConversation = new ConversationDaoJDBC();
    }

    public static DefaultServices getInstance(){

        if(instance == null)
            instance = new DefaultServices();

        return instance;

    }

    @Override
    public User addUser(User user) throws Exception {
        return null;
    }

    @Override
    public User deleteUser(User user) throws Exception {
        return null;
    }

    @Override
    public User updateUser(User user) throws Exception {
        return null;
    }

    @Override
    public List<User> getUsers() throws Exception {
        return null;
    }

    @Override
    public Message addMessage(Message message) throws Exception {
        return null;
    }

    @Override
    public Message deleteMessage(Message message) throws Exception {
        return null;
    }

    @Override
    public Conversation addConversation(Conversation conversation) throws Exception {
        return null;
    }

    @Override
    public Conversation deleteConversation(Conversation conversation) throws Exception {
        return null;
    }
}
