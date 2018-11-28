package com.jl.test.java;

import java.io.File;

/**
 * 对文件内容进行重命名
 */
public class fileRename {
    public static void main(String[] args) {
        File file1 = new File("C:\\Users\\admin\\Desktop\\old");
        File[] files = file1.listFiles();
        String path = "C:\\Users\\admin\\Desktop\\new\\";
        for (File file : files) {
            String name = file.getName();
            if (name.indexOf('_') > -1) {
                String newName = name.split("_")[0];
                String etx = name.split("\\.")[1];
                file.renameTo(new File(path + newName + "." + etx));
            } else {
                file.renameTo(new File(path + name));
            }
        }
    }
}
