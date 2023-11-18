package com.chatapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlSession {
    private static MysqlSession mysqlsession ;
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/chatapp";
    private final String user = "root";
    private final String password = "";
    private MysqlSession() {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);

        }catch(Exception e){
            e.getMessage();
        }
    }

    public static MysqlSession getInstance() {

        if(mysqlsession == null)
            mysqlsession = new MysqlSession();

        return mysqlsession;

    }
    public Connection getConnection() {
        try {

            if(connection == null || connection.isClosed())
                connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {

            e.getMessage();
        }

        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
