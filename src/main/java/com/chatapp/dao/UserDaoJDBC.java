package com.chatapp.dao;

import com.chatapp.beans.User;
import com.chatapp.utils.MysqlSession;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserDaoJDBC implements IUser{
    private MysqlSession mysqlSession;

    public UserDaoJDBC() {
        mysqlSession = MysqlSession.getInstance();
    }

    @Override
    public User addUser(User user) throws Exception {


        return null;
    }

    @Override
    public User deleteUser(User user) throws Exception {
        return null;
    }

    @Override
    public User updateUser(User user) throws Exception {
        return null;
    }

    @Override
    public User getUser(long id) throws Exception {
        return null;
    }

    @Override
    public List<User> getUsers() throws Exception {
        return null;
    }
}
