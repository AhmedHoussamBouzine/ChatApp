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
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class ConversationDaoJDBC  implements  IConversation{

    private MysqlSession mysqlSession;

    public ConversationDaoJDBC() {
        mysqlSession = MysqlSession.getInstance();
    }

    @Override
    public Conversation addConversation(Conversation conversation) throws Exception {
        try{
            Connection connection = mysqlSession.getConnection();
            String query = "insert into conversations (name,senderPublicKey,receiverPublicKey) values (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, conversation.getName());
            statement.setString(2, Base64.getEncoder().encodeToString(conversation.getSenderPublicKey().getEncoded()));
            statement.setString(3, Base64.getEncoder().encodeToString(conversation.getReceiverPublicKey().getEncoded()));
            statement.execute();
            connection.close();
            return conversation;
        }catch (Exception e){
            throw e ;
        }
    }

    @Override
    public boolean deleteConversation(String id) throws Exception {
        try {
            Connection connection = mysqlSession.getConnection();
            String query = "delete from conversations where id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            statement.execute();
            connection.close();
            return true;
        }catch (Exception e){
            throw e ;
        }
    }

    @Override
    public boolean updateConversation(Conversation conversation) throws Exception {
        try{
            Connection connection = mysqlSession.getConnection();
            String query = "update conversations set senderPublicKey= ? , receiverPublicKey = ?, updatedAt=? where id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, conversation.getSenderPublicKey().toString());
            statement.setString(2, conversation.getReceiverPublicKey().toString());
            statement.setDate(3, (java.sql.Date) new Date());
            statement.execute();
            connection.close();
            return true;
        }catch (Exception e){
            throw e ;
        }
    }
    @Override
    public Conversation getConversation(long id) throws Exception {
        Connection connection = mysqlSession.getConnection() ;
        String query = "select * from conversations where id=?" ;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);
        ResultSet resultSet=statement.executeQuery();
        if(!resultSet.next())
            return null;
        Conversation conversation = new Conversation();
        conversation.setId(resultSet.getLong("id"));

        conversation.setSenderPublicKeyFromString(resultSet.getString("senderPublicKey"));
        conversation.setReceiverPublicKeyFromString(resultSet.getString("receiverPublicKey"));

        conversation.setInsertedAt(resultSet.getDate("insertedAt"));
        conversation.setInsertedAt(resultSet.getDate("insertedAt"));
        return conversation;
    }

    @Override
    public List<Conversation> getConversations() throws Exception {
        List<Conversation> conversations = new ArrayList<Conversation>();
        Connection connection=mysqlSession.getConnection();
        String query="Select * from conversations" ;

        PreparedStatement statement=connection.prepareStatement(query);
        ResultSet resultSet=statement.executeQuery();
        while(resultSet.next())
        {
            Conversation conversation = new Conversation();
            conversation.setId(resultSet.getLong("uid"));

            conversation.setSenderPublicKeyFromString(resultSet.getString("senderPublicKey"));
            conversation.setReceiverPublicKeyFromString(resultSet.getString("receiverPublicKey"));

            conversation.setInsertedAt(resultSet.getDate("insertedAt"));
            conversation.setUpdatedAt(resultSet.getDate("updatedAt"));
            conversations.add(conversation);
        }
        connection.close();
        return conversations;
    }
}
