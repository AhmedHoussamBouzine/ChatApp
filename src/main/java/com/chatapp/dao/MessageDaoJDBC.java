package com.chatapp.dao;

import com.chatapp.beans.Message;
import com.chatapp.utils.MysqlSession;

public class MessageDaoJDBC implements IMessage {
    private MysqlSession mysqlsession ;

    public MessageDaoJDBC() {
        mysqlsession = MysqlSession.getInstance();
    }

    @Override
    public Message addMessage(Message message) throws Exception {
        return null;
    }

    @Override
    public Message deleteMessage(Message message) throws Exception {
        return null;
    }
}
