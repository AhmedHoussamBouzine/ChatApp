package com.chatapp.server;

import com.chatapp.beans.Message;
import com.chatapp.beans.User;
import com.chatapp.business.DefaultServices;
import com.chatapp.business.IServices;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatThread implements Runnable{

        private Socket socket;
        private static final int PORT = 9090;
        private Map<User,Socket> usersSockets = new HashMap<>();
        private List<User> users;


        private IServices iServices;

        public ChatThread(Socket socket, Map<User, Socket> users) {
            this.socket = socket;
            this.usersSockets = users;
            iServices = new DefaultServices();
            try {
                this.users = iServices.getUsers();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void sendMessage(Message message) {

        }

        @Override
        public void run() {
            try {
                 usersSockets.put(null,socket);
                 InputStream is = socket.getInputStream();
                 ObjectInputStream input = new ObjectInputStream(is);
                 Object receivedObject = input.readObject();
                 Message receivedMessage = (Message) receivedObject;
                 usersSockets.put(receivedMessage.getSender(),socket);
                 sendMessage(receivedMessage);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            while(true){
                for (User user: users) {
                    usersSockets.put(user,socket);
                }
            }
    }
}
