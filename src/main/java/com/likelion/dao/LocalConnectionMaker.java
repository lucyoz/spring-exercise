package com.likelion.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LocalConnectionMaker implements ConnectionMaker{
    public Connection makeConnection() throws SQLException {

        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/likelion-db",
                "root", "password");
        return c;
    }
}
