package com.chatapp.dao;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;

import java.util.List;

public interface IConversation {
    public Conversation addConversation(Conversation conversation) throws Exception ;
    public Conversation deleteConversation(Conversation conversation) throws Exception;
    public Conversation updateConversation(Conversation conversation) throws Exception;
    public Conversation getConversation(long id) throws Exception;
    public List<Conversation> getConversations() throws Exception;
}
