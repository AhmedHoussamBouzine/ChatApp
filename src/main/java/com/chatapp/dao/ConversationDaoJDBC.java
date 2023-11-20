package com.chatapp.dao;

import com.chatapp.beans.Conversation;
import com.chatapp.utils.MysqlSession;

import java.util.List;

public class ConversationDaoJDBC  implements  IConversation{

    private MysqlSession mysqlSession;

    public ConversationDaoJDBC() {
        mysqlSession = MysqlSession.getInstance();
    }

    @Override
    public Conversation addConversation(Conversation conversation) throws Exception {
        return null;
    }

    @Override
    public boolean deleteConversation(long id) throws Exception {
        return true;
    }

    @Override
    public boolean updateConversation(long id) throws Exception {
        return true;
    }
    @Override
    public Conversation getConversation(long id) throws Exception {
        return null;
    }

    @Override
    public List<Conversation> getConversations() throws Exception {
        return null;
    }
}
