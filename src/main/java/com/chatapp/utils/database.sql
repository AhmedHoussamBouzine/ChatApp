CREATE TABLE users (
                       uid BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       telephone VARCHAR(20),
                       publicKey TEXT,
                       derivedKey BLOB,
                       insertedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE conversations (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               name TEXT,
                               senderId BIGINT,
                               receiverId BIGINT,
                               insertedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               FOREIGN KEY (senderId) REFERENCES users(uid),
                               FOREIGN KEY (receiverId) REFERENCES users(uid)
);


CREATE TABLE messages (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          senderId BIGINT,
                          receiverId BIGINT,
                          content TEXT,
                          insertedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          conversationId BIGINT,
                          FOREIGN KEY (senderId) REFERENCES users(uid),
                          FOREIGN KEY (receiverId) REFERENCES users(uid),
                          FOREIGN KEY (conversationId) REFERENCES conversations(id)
);
