package com.jl.test.java;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: shudj
 * Time: 2018/11/29 11:48
 * Description:
 */
public class ReadCsv {

    /**
     * @Author shudj
     * @Date 13:52 2018/11/29
     * @Description 读取csv文件
     *
     * @param path
     * @return void
     **/
    public static List<String[]> readCsvFile(String path) {
        try {
            List<String[]> csv = new ArrayList<>();
            CsvReader reader = new CsvReader(path, ',', Charset.forName("GBK"));

            // 跳过表头   如果需要表头的话，不要写这句
            //reader.readHeaders();
            while (reader.readRecord()) {
                csv.add(reader.getValues());
            }
            reader.close();

            return csv;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap<String, String> readCsv2map(String path) throws Exception {
        List<String[]> csv = new ArrayList<>();
        CsvReader reader = new CsvReader(path, ' ', Charset.forName("GBK"));

        // 跳过表头   如果需要表头的话，不要写这句
        //reader.readHeaders();
        while (reader.readRecord()) {
            csv.add(reader.getValues());
        }
        reader.close();
        HashMap<String, String> hm = new HashMap<>();
        for (int i = 0; i < csv.size(); i++) {
            hm.put(csv.get(i)[0], csv.get(i)[2]);
        }
        
        return hm;
    }

    /**
     * @Author shudj 
     * @Date 10:52 2018/11/30
     * @Description
     *
     * @param path 文件路径
    * @param res
    * @param resNull 
     * @return void
     **/
    public static void csvWrite(String path, List<String[]> res, List<String[]> resNull){
        try {
            //日文编码 SJIS
            CsvWriter wr =new CsvWriter(path,',',Charset.forName("GBK"));
            for (String[] contents : res) {
                wr.writeRecord(contents);
            }
            wr.close();

            // 逗号隔开
            CsvWriter wrNull =new CsvWriter("C:\\Users\\admin\\Desktop\\test1_130.csv", ',',Charset.forName("GBK"));
            for (String[] contents : resNull) {
                wrNull.writeRecord(contents);
            }
            wrNull.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        String path = "C:\\Users\\admin\\Desktop\\result.csv";
        /*
         * (?i) 表示所在位置右侧的表达式开启忽略大小写模式
         * (?s) 表示所在位置右侧的表达式开启单行模式
         * (?m) 表示所在位置右侧的表示式开启指定多行模式
         * (?is) 更改句点字符 (.) 的含义，以使它与每个字符（而不是除 \n 之外的所有字符）匹配
         * (?im) 更改 ^ 和 $ 的含义，以使它们分别与任何行的开头和结尾匹配,而不只是与整个字符串的开头和结尾匹配
         **/
        // 匹配以csv或CSV结尾
        System.out.println(path.matches("^.+\\.(?i)(csv)$"));

        List<String[]> csv = readCsvFile(path);
        Connection conn = DBConnection.getConnection();
        Statement stat = conn.createStatement();
        ResultSet rs = null;
        List<String[]> res = new ArrayList<>();
        List<String[]> resNull = new ArrayList<>();
        /*HashMap<String, String> hm = readCsv2map("C:\\Users\\admin\\Desktop\\count137.csv");
        for (int i = 0; i < csv.size(); i++) {
            String code = csv.get(i)[0];
            String count1 = csv.get(i)[1];
            String count2 = csv.get(i)[2];
            String count3 = csv.get(i)[3];
            String count4 = csv.get(i)[4];
            rs = stat.executeQuery("select name,image0 from skuinfo where isdel = 0 and code =" + code);
            String[] contents = null;
            // 如果查到值
            if(rs.next()) {
                contents = new String[]{rs.getString("name"), code, count1, count2, count3, count4, hm.get(code),rs.getString("image0")};
                res.add(contents);
            // 查不到值
            } else {
                contents = new String[]{"", code, count1, count2, count3, count4, hm.get(code)};
                resNull.add(contents);
            }
        }*/

        rs = stat.executeQuery("select name,code from skuinfo where isdel = 0");
        while (rs.next()) {
            res.add(new String[]{rs.getString("name").replace(",", "，"), rs.getString("code")});
        }
        rs.close();
        stat.close();
        conn.close();

        csvWrite("C:\\Users\\admin\\Desktop\\name2code.csv", res, resNull);
    }
}
