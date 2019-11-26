package com.jl.test.scala.kylin

import java.util.Properties

import org.apache.kylin.jdbc.Driver

/**
  * @author: shudj
  * @time: 2019/8/30 11:19
  * @description:
  */
object Kylin_test {
    def main(args: Array[String]): Unit = {
        val driver = Class.forName("org.apache.kylin.jdbc.Driver").newInstance().asInstanceOf[Driver]
        val properties = new Properties()

        properties.put("user", "ADMIN")
        properties.put("password", "KYLIN")

        val conn = driver.connect("jdbc:kylin://192.168.50.8:7070/kylo_test", properties)
        val statement = conn.createStatement()

        val set = statement.executeQuery("select store_code, sum(real_amount) from flow_detail where part_day between'2019-08-01' and '2019-08-10' group by 1 limit 10")

        while (set.next()) {
            println(set.getString(1))
            println(set.getDouble(2))
        }
    }
}
