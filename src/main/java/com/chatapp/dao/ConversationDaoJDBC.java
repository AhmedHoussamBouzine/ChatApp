package com.chatapp.dao;

import com.chatapp.beans.Conversation;
import com.chatapp.utils.MysqlSession;

public class ConversationDaoJDBC implements IConversation{

    private MysqlSession mysqlSession;

    public ConversationDaoJDBC() {
        mysqlSession = MysqlSession.getInstance();
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
