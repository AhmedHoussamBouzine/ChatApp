package com.chatapp.server;

import com.chatapp.beans.Message;
import com.chatapp.beans.User;
import com.chatapp.business.DefaultServices;
import com.chatapp.business.IServices;
import com.chatapp.presentation.controllers.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyAgreement;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.util.*;

public class Server {

    /* Setting up variables */
    private static final int PORT = 9090;
    static Logger logger = LoggerFactory.getLogger(Server.class);
    static IServices services = new DefaultServices();
    public static void main(String[] args) throws Exception {
        logger.info("The chat server is running.");
        Map<String,Socket> users = new HashMap<>();
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                Socket socket = listener.accept();
                new Handler(socket,users).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            listener.close();
        }
    }
}