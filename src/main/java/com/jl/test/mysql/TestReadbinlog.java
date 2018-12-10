package com.jl.test.mysql;

import com.jl.test.java.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Author: shudj
 * Time: 2018/12/7 14:17
 * Description:
 */
public class TestReadbinlog {
    public static void main(String[] args) throws SQLException {
        Connection conn = DBConnection.getConnectionForPro();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("show BINARY logs");
        while (rs.next()) {
            System.out.println(rs.getString(1) + "\t" + rs.getString(2));

            return;
        }
    }
}
