package com.chatapp.dao;

import com.chatapp.beans.Message;
import com.chatapp.utils.MysqlSession;

import java.util.List;

public class MessageDaoJDBC implements IMessage{

    private MysqlSession mysqlSession;

    public MessageDaoJDBC() {
        mysqlSession = MysqlSession.getInstance();
    }

    @Override
    public Message addMessage(Message message) throws Exception {
        return null;
    }

    @Override
    public boolean deleteMessage(long id) throws Exception {
        return true;
    }

    @Override
    public boolean updateMessage(long id) throws Exception {
        return true;
    }

    @Override
    public Message getMessage(long id) throws Exception {
        return null;
    }

    @Override
    public List<Message> getMessages() throws Exception {
        return null;
    }
}
