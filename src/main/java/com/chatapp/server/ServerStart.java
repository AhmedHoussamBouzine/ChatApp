package com.chatapp.server;

import com.chatapp.beans.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerStart {

    private final static int PORT = 9090;
    public static void main(String[] args) {
            try {
                Map<User, Socket> users = new HashMap<>();
                ServerSocket serverSocket = new ServerSocket(PORT);
                while (true){
                    Socket socket = serverSocket.accept();
                    ChatThread chatThread = new ChatThread(socket,users);
                    new Thread(chatThread).start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
}

