package com.chatapp.dao;

import com.chatapp.beans.User;

import java.util.List;

public interface IUser {
    public User addUser(User user) throws  Exception;
    public User deleteUser(User user) throws Exception;
    public User updateUser(User user) throws Exception;
    public List<User> getUsers() throws Exception;

}
