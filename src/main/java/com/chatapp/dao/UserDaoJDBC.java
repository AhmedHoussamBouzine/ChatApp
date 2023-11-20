package com.chatapp.dao;

import com.chatapp.beans.User;
import com.chatapp.utils.MysqlSession;

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
    public boolean deleteUser(long id) throws Exception {
        return true;
    }

    @Override
    public boolean updateUser(long id) throws Exception {
        return true;
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
