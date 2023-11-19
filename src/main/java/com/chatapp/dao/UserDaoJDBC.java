package com.chatapp.dao;

import com.chatapp.beans.User;
import com.chatapp.utils.MysqlSession;

import java.util.List;

public class UserDaoJDBC implements IUser {
    private MysqlSession mysqlsession ;

    public UserDaoJDBC() {
        mysqlsession = MysqlSession.getInstance();
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
    public List<User> getUsers() throws Exception {
        return null;
    }
}
