package com.jl.test.java;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqlToTxt {
    public static void main(String[] args) throws Exception {

        Connection conn = DBConnection.getConnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select name, code from skuinfo where isdel !=1");
        File file = new File("C:\\Users\\admin\\Desktop\\365.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        int count = 0;
        StringBuffer sb = new StringBuffer();
        while (rs.next()) {

            count ++;
            sb.append(rs.getString("code") + "~" + rs.getString("name"));
            sb.append("" + "\n");
        }
        bw.write(String.valueOf(sb));
        bw.close();
        System.out.println(count);
    }
}
