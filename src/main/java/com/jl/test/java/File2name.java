package com.jl.test.java;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class File2name {

    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\admin\\Desktop\\bbl_sku1.txt");
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String str;
        HashMap<String, String> hm;
        while (true) {
            str = reader.readLine();
            if (null == str) break;
            hm = new HashMap<String, String>();
            // 截取最后的逗号
            str = str.substring(0, str.length() - 1);
            JSONObject json = JSONObject.fromObject(str);
            String item_para = String.valueOf(json.get("item_para"));
            // 获取条码
            String code = item_para.split("\"")[1];
            hm.put("code", code);
            hm.put(code, "exist");
            // 获取商品详细信息
            JSONObject json2 = JSONObject.fromObject(item_para);
            String key = String.valueOf(json2.get(code));
            JSONObject json1 = JSONObject.fromObject(key);
            String name = json1.get("itemName").toString();
            hm.put("name", String.valueOf(json1.get("itemName")));
            hm.put("unit", String.valueOf(json1.get("unit")));
            hm.put("price", String.valueOf(json1.get("salePrice")));
            list.add(hm);
        }
        File images = new File("C:\\Users\\admin\\Desktop\\everyday");
        File[] files = images.listFiles();
        for (File image : files) {
            String name = image.getName();
            String[] flags = name.split("_");
            for (HashMap<String, String> h : list) {
                if ("exist".equals(h.get(flags[0]))) {
                    h.put("image" + flags[1].substring(0, 1), "everyday/" + name);
                }
            }
        }

        StringBuffer sb = new StringBuffer();
        for (HashMap<String, String> h : list) {
            sb.append("(\"" + h.getOrDefault("name", "''") + "\",");
            sb.append("002" + ",");
            sb.append("\"" + h.getOrDefault("unit", "''") + "\",");
            sb.append("\"" + h.getOrDefault("price", "''") + "\",");
            sb.append("\"" + h.getOrDefault("code", "''") + "\",");
            sb.append("\"" + h.getOrDefault("image0", "''") + "\",");
            sb.append("\"" + h.getOrDefault("image1", "''") + "\",");
            sb.append("\"" + h.getOrDefault("image2", "''") + "\"),");
        }
        Connection conn = DBConnection.getConnection();
        Statement stat = conn.createStatement();
        String sql = "insert into sku.skuinfo values" + sb.substring(0, sb.length() - 1);
        System.out.println(sql);
        stat.execute(sql);
        stat.close();
        conn.close();
        System.out.println(list.size());
    }
}
