package com.chatapp.dao;

import com.chatapp.beans.Conversation;

public interface IConversation {

    public Conversation addConversation(Conversation conversation) throws  Exception;
    public Conversation deleteConversation(Conversation conversation) throws Exception;
}
