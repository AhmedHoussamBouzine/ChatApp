package com.chatapp.dao;

import com.chatapp.beans.User;
import com.chatapp.utils.MysqlSession;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class UserDaoJDBC implements IUser{
    private MysqlSession mysqlSession;

    public UserDaoJDBC() {
        mysqlSession = MysqlSession.getInstance();
    }

    @Override
    public User addUser(User user) throws Exception {
        Connection connection = mysqlSession.getConnection();
        String query = "INSERT INTO users (username, email, password, telephone, publicKey) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getEmail());
        statement.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        statement.setString(4, user.getTelephone());
        statement.setString(5, Base64.getEncoder().encodeToString(user.getPublicKey().getEncoded()));
        statement.execute();
        connection.close();
        return user;
    }


    @Override
    public boolean deleteUser(long id) throws Exception {
        Connection connection=mysqlSession.getConnection();
        String query="delete from users where uid = ?";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setLong(1, id);
        statement.execute();
        connection.close();
        return true;
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        Connection connection=mysqlSession.getConnection();
        String query="update users set userName= ? , telephone = ? , updatedAt = ? where uid = ?;";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getTelephone());
        statement.setDate(3, (java.sql.Date) new Date());
        statement.setLong(4, user.getUid());
        statement.execute();
        connection.close();
        return true;
    }

    @Override
    public User getUser(long id) throws Exception {
        Connection connection=mysqlSession.getConnection();
        String query="select * from users where uid=?";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setLong(1, id);
        ResultSet resultSet=statement.executeQuery();
        if(!resultSet.next())
            return null;
        User user=new User();
        user.setUid(resultSet.getLong("uid"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setTelephone(resultSet.getString("telephone"));
        user.setPublicKeyFromString(resultSet.getString("publicKey"));
        user.setInsertedAt(resultSet.getTimestamp("insertedAt").toLocalDateTime());
        user.setInsertedAt(resultSet.getTimestamp("insertedAt").toLocalDateTime());
        return user;
    }

    @Override
    public User getUserByUsername(String username) throws Exception {
        Connection connection=mysqlSession.getConnection();
        String query="select * from users where username=?";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet=statement.executeQuery();
        if(!resultSet.next())
            return null;
        User user=new User();
        user.setUid(resultSet.getLong("uid"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setTelephone(resultSet.getString("telephone"));
        user.setPublicKeyFromString(resultSet.getString("publicKey"));
        user.setInsertedAt(resultSet.getTimestamp("insertedAt").toLocalDateTime());
        user.setInsertedAt(resultSet.getTimestamp("insertedAt").toLocalDateTime());
        return user;
    }

    @Override
    public List<User> getUsers() throws Exception {
        List<User> users=new ArrayList<User>();

        Connection connection=mysqlSession.getConnection();
        String query="Select * from users";
        PreparedStatement statement=connection.prepareStatement(query);
        ResultSet resultSet=statement.executeQuery();
        while(resultSet.next())
        {
            User user=new User();
            user.setUid(resultSet.getLong("uid"));
            user.setUsername(resultSet.getString("username"));
            user.setEmail(resultSet.getString("email"));
            user.setTelephone(resultSet.getString("telephone"));
            user.setPublicKeyFromString(resultSet.getString("publicKey"));
            user.setInsertedAt(resultSet.getTimestamp("insertedAt").toLocalDateTime());
            user.setInsertedAt(resultSet.getTimestamp("insertedAt").toLocalDateTime());
            users.add(user);
        }
        connection.close();
        return users;
    }
}
