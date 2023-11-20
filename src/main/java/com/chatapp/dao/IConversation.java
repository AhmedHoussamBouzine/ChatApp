package com.chatapp.dao;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;

import java.util.List;

public interface IConversation {
    public Conversation addConversation(Conversation conversation) throws Exception ;
    public boolean deleteConversation(long id) throws Exception;
    public boolean updateConversation(long id) throws Exception;
    public Conversation getConversation(long id) throws Exception;
    public List<Conversation> getConversations() throws Exception;
}
