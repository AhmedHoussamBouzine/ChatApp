CREATE TABLE conversations (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               name TEXT,
                               senderPublicKey TEXT,
                               receiverPublicKey TEXT,
                               insertedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE users (
                       uid BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       telephone VARCHAR(20),
                       publicKey TEXT,
                       insertedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE messages (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          senderId BIGINT,
                          receiverId BIGINT,
                          content TEXT,
                          insertedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          conversationId BIGINT, -- Adding conversationId as a foreign key
                          FOREIGN KEY (senderId) REFERENCES users(uid),
                          FOREIGN KEY (receiverId) REFERENCES users(uid),
                          FOREIGN KEY (conversationId) REFERENCES conversations(id)
);
