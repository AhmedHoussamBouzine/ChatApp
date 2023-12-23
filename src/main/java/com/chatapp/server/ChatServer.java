package com.chatapp.server;

import com.chatapp.beans.User;
import com.chatapp.business.DefaultServices;
import com.chatapp.business.IServices;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 9090;
    private static final Map<String, Socket> connectedClients = new HashMap<>();
    private static final Map<String, User> users = new HashMap<>();

    private static IServices iServices;


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            String username = input.readLine(); // Receive the username from client
            System.out.println(username + " has connected.");
            iServices = new DefaultServices();
            User user = iServices.getUser(1);
            users.put(username, user);
            connectedClients.put(username, clientSocket);

            String message;
            while ((message = input.readLine()) != null) {
                System.out.println(username + ": " + message); // Display messages in server console

                String[] parts = message.split(":", 2);
                if (parts.length >= 2) {
                    String recipient = parts[0].trim();
                    String msgContent = parts[1].trim();

                    Socket recipientSocket = connectedClients.get(recipient);
                    if (recipientSocket != null) {
                        PrintWriter recipientOutput = new PrintWriter(recipientSocket.getOutputStream(), true);
                        recipientOutput.println(username + ": " + msgContent);
                    } else {
                        output.println("User '" + recipient + "' is not online.");
                    }
                }
            }


            clientSocket.close();
            connectedClients.remove(username);
            users.remove(username);
            System.out.println(username + " has disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
