package com.jl.test.java;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class ProductsFor365 {
    public static void main(String[] args) throws Exception {
        String path = "C:\\Users\\admin\\Desktop\\new";
        String path1 = "C:\\Users\\admin\\Desktop\\365\\";
        File file = new File(path);
        File[] files = file.listFiles();
        // 构建hash根据code找图片
        HashMap<String, String> hm = new HashMap<>();
        for(File file1 : files) {
            String name = file1.getName();
            hm.put(name.split("\\.")[0], name);
        }
        Connection conn = DBConnection.getConnectionForPro();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select * from goodsh");
        StringBuffer sb = new StringBuffer();
        while (rs.next()) {
            String name = rs.getString("name");
            String code = rs.getString("code");
            String code2 = rs.getString("code2");
            // name
            sb.append("(\"" + name + "\",");
            sb.append("001" + ",");
            // unit
            sb.append("'',");
            // price
            sb.append("0.0,");
            String key = "";
            String image;
            if (null == code2) {
                key = code;
                image = hm.getOrDefault(code, "");
            } else {
                key = code2;
                image = hm.getOrDefault(code2, "").equals("") ?
                        hm.getOrDefault(code, "") :
                        hm.getOrDefault(code2, "");
            }
            // 条码
            sb.append("\"" + key + "\",");
            if (!"".equals(image)) {
                new File(path + "\\" + image).renameTo(
                        new File(path1 + key + "." + image.split("\\.")[1]));
                // image0
                sb.append("'365/" + image + "',");
            } else {
                // image0
                sb.append("'',");
            }
            // image1
            sb.append("'',");
            // image2
            sb.append("''),");
        }

        rs.close();
        stat.close();
        conn.close();
        Connection conn1 = DBConnection.getConnection();
        Statement stat1 = conn1.createStatement();
        String sql = "insert into sku.skuinfo values" + sb.substring(0, sb.length() - 1);
        System.out.println(sql);
        stat1.execute(sql);
        stat1.close();
        conn1.close();
    }
}
