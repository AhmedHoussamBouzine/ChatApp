package com.chatapp.dao;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.User;
import com.chatapp.utils.MysqlSession;
import org.mindrot.jbcrypt.BCrypt;

import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationDaoJDBC  implements  IConversation{

    private MysqlSession mysqlSession;

    public ConversationDaoJDBC() {
        mysqlSession = MysqlSession.getInstance();
    }

    @Override
    public Conversation addConversation(Conversation conversation) throws Exception {
        Connection connection = mysqlSession.getConnection() ;
        String query = "insert into conversations values (null,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, conversation.getSenderPublicKey().toString());
        statement.setString(2, conversation.getReceiverPublicKey().toString());
        statement.setDate(3, (java.sql.Date) new Date());
        statement.setDate(4, (java.sql.Date) new Date());
        statement.execute();
        connection.close();
        return conversation;
    }

    @Override
    public boolean deleteConversation(String id) throws Exception {
        Connection connection=mysqlSession.getConnection();
        String query="delete from conversations where id = ?";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setString(1, id);
        statement.execute();
        connection.close();
        return true;
    }

    @Override
    public boolean updateConversation(Conversation conversation) throws Exception {
        Connection connection = mysqlSession.getConnection();
        String query ="update conversations set senderPublicKey= ? , receiverPublicKey = ?, updatedAt=? where id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, conversation.getSenderPublicKey().toString());
        statement.setString(2, conversation.getReceiverPublicKey().toString());
        statement.setDate(3, (java.sql.Date) new Date());
        statement.execute();
        connection.close();
        return true;
    }
    @Override
    public Conversation getConversation(String id) throws Exception {
        Connection connection = mysqlSession.getConnection() ;
        String query = "select * from conversations where uid=?" ;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id);
        ResultSet resultSet=statement.executeQuery();
        if(!resultSet.next())
            return null;
        Conversation conversation = new Conversation();
        conversation.setId(resultSet.getString("uid"));

        //I need a methode to transform the public key from a string to an object of class "PublicKey"
        PublicKey senderPublicKey = PublicKey.fromString(resultSet.getString("senderPublicKey"));
        PublicKey receiverPublicKey = PublicKey.fromString(resultSet.getString("receiverPublicKey"));

        conversation.setSenderPublicKey(senderPublicKey);
        conversation.setReceiverPublicKey(receiverPublicKey);

        conversation.setInsertedAt(resultSet.getDate("insertedAt"));
        conversation.setInsertedAt(resultSet.getDate("insertedAt"));
        return conversation;
    }

    @Override
    public List<Conversation> getConversations() throws Exception {
        List<Conversation> conversations = new ArrayList<Conversation>();
        Connection connection=mysqlSession.getConnection();
        String query="Select * from conversations";

        PreparedStatement statement=connection.prepareStatement(query);
        ResultSet resultSet=statement.executeQuery();
        while(resultSet.next())
        {
            Conversation conversation = new Conversation();
            conversation.setId(resultSet.getString("uid"));

            //need to define what is fromString methode or replace it functionality
            PublicKey senderPublicKey = PublicKey.fromString(resultSet.getString("senderPublicKey"));
            PublicKey receiverPublicKey = PublicKey.fromString(resultSet.getString("receiverPublicKey"));

            conversation.setSenderPublicKey(senderPublicKey);
            conversation.setReceiverPublicKey(receiverPublicKey);

            conversation.setInsertedAt(resultSet.getDate("insertedAt"));
            conversation.setUpdatedAt(resultSet.getDate("updatedAt"));
            conversations.add(conversation);
        }
        connection.close();
        return conversations;
    }
}
