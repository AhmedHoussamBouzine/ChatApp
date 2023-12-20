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
            String query = "insert into conversations (name,senderId,receiverId) values (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, conversation.getName());
            statement.setLong(2, conversation.getSender().getUid());
            statement.setLong(3, conversation.getReceiver().getUid());
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
            String query = "update conversations set senderId= ? , receiverId = ?, updatedAt=? where id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, conversation.getSender().getUid());
            statement.setLong(2, conversation.getReceiver().getUid());
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
        conversation.setName(resultSet.getString("name"));
        String querySender="select * from users where uid=?";
        PreparedStatement statementSender=connection.prepareStatement(querySender);
        statementSender.setLong(1, resultSet.getLong("senderId"));
        ResultSet resultSetSender=statementSender.executeQuery();
        if(!resultSetSender.next())
            return null;
        User sender=new User();
        sender.setUid(resultSetSender.getLong("uid"));
        sender.setUsername(resultSetSender.getString("username"));
        sender.setEmail(resultSetSender.getString("email"));
        sender.setTelephone(resultSetSender.getString("telephone"));
        sender.setPublicKeyFromString(resultSetSender.getString("publicKey"));
        conversation.setSender(sender);
        String queryReceiver="select * from users where uid=?";
        PreparedStatement statementReceiver=connection.prepareStatement(queryReceiver);
        statementReceiver.setLong(1, resultSet.getLong("receiverId"));
        ResultSet resultSetReceiver=statementReceiver.executeQuery();
        if(!resultSetReceiver.next())
            return null;
        User receiver=new User();
        receiver.setUid(resultSetReceiver.getLong("uid"));
        receiver.setUsername(resultSetReceiver.getString("username"));
        receiver.setEmail(resultSetReceiver.getString("email"));
        receiver.setTelephone(resultSetReceiver.getString("telephone"));
        receiver.setPublicKeyFromString(resultSetReceiver.getString("publicKey"));
        conversation.setReceiver(receiver);
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
            conversation.setId(resultSet.getLong("id"));
            String querySender="select * from users where uid=?";
            PreparedStatement statementSender=connection.prepareStatement(querySender);
            statementSender.setLong(1, resultSet.getLong("senderId"));
            ResultSet resultSetSender=statementSender.executeQuery();
            if(!resultSetSender.next())
                return null;
            User sender=new User();
            sender.setUid(resultSet.getLong("uid"));
            sender.setUsername(resultSet.getString("username"));
            sender.setEmail(resultSet.getString("email"));
            sender.setTelephone(resultSet.getString("telephone"));
            sender.setPublicKeyFromString(resultSet.getString("publicKey"));
            conversation.setSender(sender);
            String queryReceiver="select * from users where uid=?";
            PreparedStatement statementReceiver=connection.prepareStatement(queryReceiver);
            statementReceiver.setLong(1, resultSet.getLong("receiverId"));
            ResultSet resultSetReceiver=statementReceiver.executeQuery();
            if(!resultSetReceiver.next())
                return null;
            User receiver=new User();
            receiver.setUid(resultSet.getLong("uid"));
            receiver.setUsername(resultSet.getString("username"));
            receiver.setEmail(resultSet.getString("email"));
            receiver.setTelephone(resultSet.getString("telephone"));
            receiver.setPublicKeyFromString(resultSet.getString("publicKey"));
            conversation.setSender(receiver);
            conversation.setInsertedAt(resultSet.getDate("insertedAt"));
            conversation.setInsertedAt(resultSet.getDate("insertedAt"));
            conversations.add(conversation);
        }
        connection.close();
        return conversations;
    }
}
