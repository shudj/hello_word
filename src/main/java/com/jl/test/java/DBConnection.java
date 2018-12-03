package com.jl.test.java;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUsername("root");
            dataSource.setPassword("123456");
            dataSource.setUrl("jdbc:mysql://192.168.1.130:3306/sku?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false");
            Connection connection = dataSource.getConnection();
            return connection;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Connection getConnectionForPro() throws SQLException {
        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUsername("root");
            dataSource.setPassword("julong");
            dataSource.setUrl("jdbc:mysql://localhost:3306/product?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false");
            Connection connection = dataSource.getConnection();
             return connection;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
