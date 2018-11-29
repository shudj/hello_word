package com.jl.test.java;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;
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
            CsvReader reader = new CsvReader(path, ',', Charset.forName("GBK"));

            // 跳过表头   如果需要表头的话，不要写这句
            reader.readHeaders();
            while (reader.readRecord()) {
                csv.add(reader.getValues());
            }
            reader.close();

            for (int i = 0; i < csv.size(); i++) {
                System.out.print(csv.get(i)[0] + "\t");
                System.out.print(csv.get(i)[1] + "\t");
                System.out.print(csv.get(i)[2]);
                System.out.println();
                if (i == 7) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void csvWrite(String path){
        try {
            //日文编码 SJIS
            CsvWriter wr =new CsvWriter(path,',',Charset.forName("GBK"));
            String[] contents = {"警告信息","非法操作","没有权限","操作失败"};
            wr.writeRecord(contents);
            wr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String path = "C:\\Users\\admin\\Desktop\\tmp.csv";
        /*
         * (?i) 表示所在位置右侧的表达式开启忽略大小写模式
         * (?s) 表示所在位置右侧的表达式开启单行模式
         * (?m) 表示所在位置右侧的表示式开启指定多行模式
         * (?is) 更改句点字符 (.) 的含义，以使它与每个字符（而不是除 \n 之外的所有字符）匹配
         * (?im) 更改 ^ 和 $ 的含义，以使它们分别与任何行的开头和结尾匹配,而不只是与整个字符串的开头和结尾匹配
         **/
        // 匹配以csv或CSV结尾
        System.out.println(path.matches("^.+\\.(?i)(csv)$"));
        // readCsvFile(path);
        csvWrite("C:\\Users\\admin\\Desktop\\test.csv");
    }
}
