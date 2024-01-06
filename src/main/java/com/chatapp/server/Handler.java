package com.chatapp.server;

import com.chatapp.beans.Message;
import com.chatapp.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyAgreement;
import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Handler extends Thread{

        private String name;
        private Socket socket;
        private Logger logger = LoggerFactory.getLogger(Handler.class);

        Map<String,Socket> users = new HashMap<>();
        public Handler(Socket socket,Map<String,Socket> users){
            this.socket = socket;
            this.users = users;
        }
        public void receiveUsername(){
            InputStream inputStream = null;
            try {
                inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String user = bufferedReader.readLine();
                logger.info("connect to user : " + user);
                name = user;
                users.put(user,socket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        private void sendMessage(Message message) {
            if (message.getSender().getUid() == message.getReceiver().getUid()){
                users.keySet().forEach(key -> {
                    if (!Objects.equals(key, message.getReceiver().getUsername())) {
                        Socket destSocket = users.get(key);
                        try {
                            OutputStream outputStream = destSocket.getOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.writeObject(message);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }else{
                Socket destSocket = users.get(message.getReceiver().getUsername());
                try {
                    OutputStream outputStream = destSocket.getOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void run() {
            logger.info("Attempting to connect a user...");
            try {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                ObjectInputStream ois = null;
                receiveUsername();
                while (true) {
                    System.out.println(socket);
                    inputStream = socket.getInputStream();
                    System.out.println(inputStream);
                    ois = new ObjectInputStream(inputStream);
                    Message message = (Message) ois.readObject();
                    performKeyExchange(message.getSender(), message.getReceiver());
                    logger.info("Received message from user: " + message.getSender().getUsername() + message.getContent());
                    sendMessage(message);
                }
            } catch (Exception e) {
                logger.error("Exception in run() method for user: " + name, e);
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

}