package com.chatapp.business;

import com.chatapp.beans.Conversation;
import com.chatapp.beans.Message;
import com.chatapp.beans.User;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import static com.chatapp.beans.Conversation.generateKeyPair;

public class Test {
    public static void main(String[] args) throws Exception {
        IServices iServices = new DefaultServices();

//        List<User> users = new ArrayList<User>();
//        users = iServices.getUsers();
//
//        for (User user:
//                users) {
//            System.out.println(user);
//        }

  //    User user = iServices.getUser(6);
        User user = new User("abdelouahad", "String email", "String password", "String telephone");
//        KeyPair keyPair = generateKeyPair();
//        PublicKey publicKey = keyPair.getPublic();
//        user.setPublicKey(publicKey);
        iServices.addUser(user);


//
//        User user1 =  iServices.getUser(1);
//        User user2 =  iServices.getUser(1);
//        Conversation conversation = iServices.getConversation(1);
//        Message message = new Message(user1,user2,"hjkshdfs");
//        message.setConversation(conversation);
//        iServices.addMessage(message);

//        iServices.deleteMessage(1);

//        Conversation conversation = new Conversation(user,user);
//        System.out.println(conversation);
//        iServices.addConversation(conversation);

//       System.out.println(iServices.getConversation(1));




    }

}
