package com.chatapp.dao;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;

import java.util.List;

public interface IConversation {
    public Conversation addConversation(Conversation conversation) throws Exception ;
    public boolean deleteConversation(String id) throws Exception;
    public boolean updateConversation(Conversation conversation) throws Exception;
    public Conversation getConversation(long id) throws Exception;
    public List<Conversation> getConversations() throws Exception;
    public Conversation getLastConversation() throws Exception;
}
