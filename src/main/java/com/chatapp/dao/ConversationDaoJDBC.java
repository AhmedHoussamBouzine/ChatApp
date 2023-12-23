package com.chatapp.dao;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;
import com.chatapp.beans.User;
import com.chatapp.business.DefaultServices;
import com.chatapp.business.IServices;
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
        IServices iServices = new DefaultServices();
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
        conversation.setSender(iServices.getUser(resultSet.getLong("senderId")));
        conversation.setReceiver(iServices.getUser(resultSet.getLong("receiverId")));
        conversation.setInsertedAt(resultSet.getDate("insertedAt"));
        conversation.setInsertedAt(resultSet.getDate("insertedAt"));
        String queryMessages = "Select * from messages where id = ?";
        PreparedStatement statementMessages=connection.prepareStatement(queryMessages);
        statementMessages.setLong(1, conversation.getId());
        ResultSet resultSetMessages=statementMessages.executeQuery();
        List<Message> messages = new ArrayList<>();
        while(resultSetMessages.next())
        {
            Message message=new Message();
            message.setId(resultSetMessages.getLong("id"));
            User sender = new User(resultSetMessages.getLong("senderId"));
            User receiver = new User(resultSetMessages.getLong("receiverId"));
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent(resultSetMessages.getString("content"));
            message.setInsertedAt(resultSet.getDate("insertedAt"));
            message.setInsertedAt(resultSet.getDate("insertedAt"));
            messages.add(message);
        }
        conversation.setInsertedAt(resultSet.getDate("insertedAt"));
        conversation.setInsertedAt(resultSet.getDate("insertedAt"));
        conversation.setMessages(messages);
        return conversation;
    }
    public Conversation getLastConversation() throws Exception {
        IServices iServices = new DefaultServices();
        Connection connection = mysqlSession.getConnection() ;
        String query = "select * from conversations ORDER BY id desc limit 1;" ;
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet=statement.executeQuery();
        if(!resultSet.next())
            return null;
        Conversation conversation = new Conversation();
        conversation.setId(resultSet.getLong("id"));
        conversation.setName(resultSet.getString("name"));
        conversation.setSender(iServices.getUser(resultSet.getLong("senderId")));
        conversation.setReceiver(iServices.getUser(resultSet.getLong("receiverId")));
        conversation.setInsertedAt(resultSet.getDate("insertedAt"));
        conversation.setInsertedAt(resultSet.getDate("insertedAt"));
        String queryMessages = "Select * from messages where id = ?";
        PreparedStatement statementMessages=connection.prepareStatement(queryMessages);
        statementMessages.setLong(1, conversation.getId());
        ResultSet resultSetMessages=statementMessages.executeQuery();
        List<Message> messages = new ArrayList<>();
        while(resultSetMessages.next())
        {
            Message message=new Message();
            message.setId(resultSetMessages.getLong("id"));
            User sender = new User(resultSetMessages.getLong("senderId"));
            User receiver = new User(resultSetMessages.getLong("receiverId"));
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent(resultSetMessages.getString("content"));
            message.setInsertedAt(resultSet.getDate("insertedAt"));
            message.setInsertedAt(resultSet.getDate("insertedAt"));
            messages.add(message);
        }
        conversation.setInsertedAt(resultSet.getDate("insertedAt"));
        conversation.setInsertedAt(resultSet.getDate("insertedAt"));
        conversation.setMessages(messages);
        return conversation;
    }



    @Override
    public List<Conversation> getConversations() throws Exception {
        IServices iServices = new DefaultServices();
        List<Conversation> conversations = new ArrayList<Conversation>();
        Connection connection=mysqlSession.getConnection();
        String query="Select * from conversations" ;

        PreparedStatement statement=connection.prepareStatement(query);
        ResultSet resultSet=statement.executeQuery();
        while(resultSet.next())
        {
            Conversation conversation = new Conversation();
            conversation.setId(resultSet.getLong("id"));
            conversation.setName(resultSet.getString("name"));
            conversation.setSender(iServices.getUser(resultSet.getLong("senderId")));
            conversation.setReceiver(iServices.getUser(resultSet.getLong("receiverId")));
            conversation.setInsertedAt(resultSet.getDate("insertedAt"));
            conversation.setInsertedAt(resultSet.getDate("insertedAt"));
            String queryMessages = "Select * from messages where conversationId = ? ORDER BY insertedAt DESC;";
            PreparedStatement statementMessages=connection.prepareStatement(queryMessages);
            statementMessages.setLong(1, conversation.getId());
            ResultSet resultSetMessages=statementMessages.executeQuery();
            List<Message> messages = new ArrayList<>();
            while(resultSetMessages.next())
            {
                Message message=new Message();
                message.setId(resultSetMessages.getLong("id"));
                User sender = new User(resultSetMessages.getLong("senderId"));
                User receiver = new User(resultSetMessages.getLong("receiverId"));
                message.setSender(sender);
                message.setReceiver(receiver);
                message.setContent(resultSetMessages.getString("content"));
                message.setInsertedAt(resultSet.getDate("insertedAt"));
                message.setInsertedAt(resultSet.getDate("insertedAt"));
                messages.add(message);
            }
            conversation.setInsertedAt(resultSet.getDate("insertedAt"));
            conversation.setInsertedAt(resultSet.getDate("insertedAt"));
            conversation.setMessages(messages);
            conversations.add(conversation);
        }
        connection.close();
        return conversations;
    }
}
