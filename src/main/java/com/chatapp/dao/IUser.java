package com.chatapp.dao;

import com.chatapp.beans.Message;
import com.chatapp.beans.User;

import java.util.List;

public interface IUser {
    public User addUser(User user) throws Exception ;
    public boolean deleteUser(long id) throws Exception;
    public boolean updateUser(long id) throws Exception;
    public User getUser(long id) throws Exception;
    public List<User> getUsers() throws Exception;
}
