package com.jl.test.mode.kafka.java.ip;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Author: shudj
 * Time: 2018/12/5 11:48
 * Description:
 */
public class PropertyReader {
    private Properties prop = null;
    public PropertyReader() {
        InputStream is = null;
        try {
            this.prop = new Properties();
            is = this.getClass().getResourceAsStream("./streaming.properties");
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPropertyValue(String key) {
        return prop.getProperty(key);
    }

    public static final String TOPIC = "topic";
    public static final String BROKER_LIST = "broker.list";
    public static final String APP_NAME = "appname";
    public static final String GROUP_ID = "group.id";

}
