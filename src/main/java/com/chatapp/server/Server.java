package com.chatapp.server;

import com.chatapp.beans.Message;
import com.chatapp.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyAgreement;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
public class Server {

    /* Setting up variables */
    private static final int PORT = 9090;
    private static final HashMap<String, User> names = new HashMap<>();
    private static HashSet<ObjectOutputStream> writers = new HashSet<>();
    private static ArrayList<User> users = new ArrayList<>();
    static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        logger.info("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);

        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            listener.close();
        }
    }

    private static void performKeyExchange(User sender, User receiver) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(2048); // Adjust key size as needed

        KeyPair senderKeyPair = keyPairGenerator.generateKeyPair();
        KeyPair receiverKeyPair = keyPairGenerator.generateKeyPair();

        KeyAgreement senderKeyAgreement = KeyAgreement.getInstance("DH");
        senderKeyAgreement.init(senderKeyPair.getPrivate());
        senderKeyAgreement.doPhase(receiverKeyPair.getPublic(), true);

        KeyAgreement receiverKeyAgreement = KeyAgreement.getInstance("DH");
        receiverKeyAgreement.init(receiverKeyPair.getPrivate());
        receiverKeyAgreement.doPhase(senderKeyPair.getPublic(), true);

        byte[] senderSharedSecret = senderKeyAgreement.generateSecret();
        byte[] receiverSharedSecret = receiverKeyAgreement.generateSecret();

        // Use a key derivation function (KDF) ratchet here (e.g., HKDF) to derive keys
        byte[] senderDerivedKey = performKDF(senderSharedSecret);
        byte[] receiverDerivedKey = performKDF(receiverSharedSecret);

        // Store derived keys in sender and receiver objects
        sender.setDerivedKey(senderDerivedKey);
        receiver.setDerivedKey(receiverDerivedKey);
    }

    private static byte[] performKDF(byte[] sharedSecret) throws Exception {
        // Implement a Key Derivation Function (KDF) here (e.g., HKDF)
        // Example using a simple hash function (SHA-256)
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(sharedSecret);
    }

    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private Logger logger = LoggerFactory.getLogger(Handler.class);
        private User user;
        private ObjectInputStream input;
        private OutputStream os;
        private ObjectOutputStream output;
        private InputStream is;

        public Handler(Socket socket) throws IOException {
            this.socket = socket;
        }

        public void run() {
            logger.info("Attempting to connect a user...");

                try {
                    is = socket.getInputStream();
                    input = new ObjectInputStream(is);
                    os = socket.getOutputStream();
                    output = new ObjectOutputStream(os);
                    while (!socket.isClosed() && !socket.isInputShutdown()) {

                        Object receivedObject = input.readObject();
                        if (receivedObject instanceof Message) {
                            Message receivedMessage = (Message) receivedObject;
                            System.out.println("Received message from user: " + receivedMessage.getContent());
                            addToList(receivedMessage);
                            String recipientUsername = receivedMessage.getReceiver().getUsername();
                            output.writeObject(receivedMessage);
                            output.close();
                            System.out.println("Message sent to user " + recipientUsername);
                        }
                        if (socket.isClosed() || socket.isInputShutdown()) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    logger.error("Exception in run() method for user: " + name, e);
                }
            }


        private Message removeFromList() throws IOException {
            logger.debug("removeFromList() method Enter");
            Message msg = new Message();
            msg.setContent("has left the chat.");
            write(msg);
            logger.debug("removeFromList() method Exit");
            return msg;
        }

        /*
         * For displaying that a user has joined the server
         */
        private Message addToList(Message msg) throws IOException {
            write(msg);
            return msg;
        }

        /*
         * Creates and sends a Message type to the listeners.
         */
        private void write(Message msg) throws IOException {
            for (ObjectOutputStream writer : writers) {
                writer.writeObject(msg);
                writer.reset();
            }
        }

        private synchronized void closeConnections()  {
            logger.debug("closeConnections() method Enter");
            logger.info("HashMap names:" + names.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            if (name != null) {
                names.remove(name);
                logger.info("User: " + name + " has been removed!");
            }
            if (user != null){
                users.remove(user);
                logger.info("User object: " + user + " has been removed!");
            }
            if (output != null){
                writers.remove(output);
                logger.info("Writer object: " + user + " has been removed!");
            }
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                removeFromList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("HashMap names:" + names.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            logger.debug("closeConnections() method Exit");
        }
    }
}