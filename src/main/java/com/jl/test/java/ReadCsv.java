package com.jl.test.java;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
    public static void readCsvFile(String path) {
        try {
            List<String[]> csv = new ArrayList<>();
            CsvReader reader = new CsvReader(path, '\t', Charset.forName("GBK"));

            // 跳过表头   如果需要表头的话，不要写这句
            reader.readHeaders();
            while (reader.readRecord()) {
                csv.add(reader.getValues());
            }
            reader.close();
            Connection conn = DBConnection.getConnection();
            Statement stat = conn.createStatement();
            ResultSet rs = null;
            List<String[]> res = new ArrayList<>();
            List<String[]> resNull = new ArrayList<>();
            for (int i = 0; i < csv.size(); i++) {
                String code = csv.get(i)[0];
                String count = csv.get(i)[1];
                rs = stat.executeQuery("select name from skuinfo where isdel = 0 and code =" + code);
                String[] contents = null;
                if(rs.next()) {
                    contents = new String[]{rs.getString("name"), code, count};
                    res.add(contents);
                } else {
                    contents = new String[]{"", code, count};
                    resNull.add(contents);
                }
            }

            csvWrite("C:\\Users\\admin\\Desktop\\test_130.csv", res, resNull);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void csvWrite(String path, List<String[]> res, List<String[]> resNull){
        try {
            //日文编码 SJIS
            CsvWriter wr =new CsvWriter(path,'\t',Charset.forName("GBK"));
            for (String[] contents : res) {
                wr.writeRecord(contents);
            }
            wr.close();

            CsvWriter wrNull =new CsvWriter("C:\\Users\\admin\\Desktop\\test1_130.csv",'\t',Charset.forName("GBK"));
            for (String[] contents : resNull) {
                wrNull.writeRecord(contents);
            }
            wrNull.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String path = "C:\\Users\\admin\\Desktop\\count原始.csv";
        /*
         * (?i) 表示所在位置右侧的表达式开启忽略大小写模式
         * (?s) 表示所在位置右侧的表达式开启单行模式
         * (?m) 表示所在位置右侧的表示式开启指定多行模式
         * (?is) 更改句点字符 (.) 的含义，以使它与每个字符（而不是除 \n 之外的所有字符）匹配
         * (?im) 更改 ^ 和 $ 的含义，以使它们分别与任何行的开头和结尾匹配,而不只是与整个字符串的开头和结尾匹配
         **/
        // 匹配以csv或CSV结尾
        System.out.println(path.matches("^.+\\.(?i)(csv)$"));
        readCsvFile(path);
    }
}
