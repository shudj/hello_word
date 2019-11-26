package com.jl.test.scala.kylin;

import org.apache.kylin.jdbc.Driver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author: shudj
 * @time: 2019/8/30 11:29
 * @description:
 */
public class Kylin_test_java {
    public static void main(String[] args) throws Exception {
        Driver driver = (Driver) Class.forName("org.apache.kylin.jdbc.Driver").newInstance();

        Properties info = new Properties();
        info.put("user", "ADMIN");
        info.put("password", "KYLIN");
        Connection conn = driver.connect("jdbc:kylin://192.168.50.8:7070/kylo_test", info);
        Statement state = conn.createStatement();
        ResultSet resultSet = state.executeQuery("select store_code, sum(real_amount) from flow_detail where part_day between'2019-08-01' and '2019-08-10' group by 1 limit 10");

        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
            System.out.println(resultSet.getDouble(2));
        }
    }
}
