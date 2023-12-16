package com.chatapp.dao;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;
import com.chatapp.beans.User;
import com.chatapp.business.DefaultServices;
import com.chatapp.business.IServices;
import com.chatapp.utils.MysqlSession;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageDaoJDBC implements IMessage{

    private MysqlSession mysqlSession;

    public MessageDaoJDBC() {
        mysqlSession = MysqlSession.getInstance();
    }

    @Override
    public Message addMessage(Message message) throws Exception {

        try{
            Connection connection = mysqlSession.getConnection();
            String query = "insert into messages (senderId, receiverId, content,conversationId) values(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, message.getSender().getUid());
            statement.setLong(2, message.getReceiver().getUid());
            statement.setString(3, message.getContent());
            statement.setLong(4, message.getConversation().getId());

            statement.execute();
            connection.close();
            return message;
        }catch (Exception e){
            throw e ;
        }
    }

    @Override
    public boolean deleteMessage(long id) throws Exception {
        Connection connection=mysqlSession.getConnection();
        String query="delete from messages where id = ?";
        PreparedStatement statement=connection.prepareStatement(query);
        /*
        //this code is replaced with the one bellow
        statement.setLong(1, id);
        statement.execute();
        connection.close();
        return true;
        */
        try {
            statement.setLong(1, id);
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0; // Return true if at least one row was deleted
        } catch (SQLException e) {
            // Handle the exception, log it, or throw a custom exception
            throw new Exception("Error deleting message with ID " + id, e);
        } finally {
            statement.close();
            connection.close();
        }
    }

    @Override
    public boolean updateMessage(Message message) throws Exception {
        try{
        Connection connection=mysqlSession.getConnection();
        String query="update messages set sender = ?, reciever=?, content=?, updatedAt = ? where uid = ?;";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setLong(1, message.getSender().getUid());
        statement.setLong(2, message.getReceiver().getUid());
        statement.setString(3, message.getContent());
        statement.setDate(4, (java.sql.Date) new Date());
        statement.execute();
        connection.close();
        return true;
        }catch (Exception e){
            throw e ;
        }

    }

    @Override
    public Message getMessage(long id) throws Exception {
        IServices iServices = new DefaultServices();
        Connection connection=mysqlSession.getConnection();
        String query="select * from messages where id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);

        ResultSet resultSet=statement.executeQuery();
        if(!resultSet.next())
            return null;
        Message message=new Message();
        message.setId(resultSet.getLong("id"));

        User sender = iServices.getUser(resultSet.getLong("senderId"));
        User receiver = iServices.getUser(resultSet.getLong("receiverId"));
        Conversation conversation = iServices.getConversation(resultSet.getLong("conversationId"));

        message.setSender( sender );
        message.setReceiver( receiver );
        message.setConversation(conversation);
        message.setContent(resultSet.getString("content"));
        return message;
    }

    @Override
    public List<Message> getMessages() throws Exception {
        List<Message> messages=new ArrayList<Message>();

        Connection connection = mysqlSession.getConnection();
        String query = "Select * from messages";
        PreparedStatement statement=connection.prepareStatement(query);
        ResultSet resultSet=statement.executeQuery();

        while(resultSet.next())
        {
            Message message=new Message();
            message.setId(resultSet.getLong("id"));

            User sender = new User(resultSet.getLong("Sender"));
            User receiver = new User(resultSet.getLong("Receiver"));

            message.setSender( sender );
            message.setReceiver( receiver );
            message.setContent(resultSet.getString("EncryptedContent"));
            messages.add(message);
        }
        connection.close();
        return messages;
    }
}
