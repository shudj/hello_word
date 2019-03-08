package com.example.tool.fileStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: shudj
 * @time: 2019/3/8 14:47
 * @description:
 */
public class FileNio {
    public static void main(String[] args) throws IOException {


    }

    /**
     * @author shudj
     * @date 15:22 2019/3/8
     * @description MMAP读取文件和写入文件内容
     *
      * @param path
     * @return void
     **/
    public void mmap(String path) throws IOException {
        FileChannel fileChannel = new RandomAccessFile(new File(path), "rw").getChannel();
        // 创建MMAP的方式
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileChannel.size());
        /*
         * 写
         **/
        byte[] data = new byte[4];
        int position = 8;
        // 从当前mmap指针位置写入4b的数据
        mappedByteBuffer.put(data);
        // 从指定 position 写入4b数据
        MappedByteBuffer subBuffer = (MappedByteBuffer) mappedByteBuffer.slice();
        subBuffer.position(position);
        subBuffer.put(data);

        /**
         * 读
         **/
        byte[] data_read = new byte[4];
        int position_read = 8;
        // 从当前 mmap指针的位置读取4b数据
        mappedByteBuffer.get(data);
        // 从position位置读取数据
        MappedByteBuffer readBuffer = (MappedByteBuffer) mappedByteBuffer.slice();
        readBuffer.position(position_read);
        readBuffer.get(data);
    }

    /**
     * @author shudj
     * @date 15:23 2019/3/8
     * @description 使用FileChannel读取和写入文件内容
     *
     * @param path
     * @return void
     **/
    public void fileChannel(String path) throws IOException {
        // 常见FileChannel
        FileChannel fileChannel = new RandomAccessFile(new File(path), "rw").getChannel();
        /*
         * 写
         **/
        // 一般都默认4096字节
        byte[] data = new byte[4096];
        long position = 1024L;
        // 从指定position开始写入4kb的数据
        fileChannel.write(ByteBuffer.wrap(data), position);
        // 从当前位置写入4kb数据
        fileChannel.write((ByteBuffer.wrap(data)));

        /*
         * 读
         **/
        // 给ByteBuffer分配4096大小
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        long position_read = 1024L;
        // 指定position读取4kb数据
        fileChannel.read(buffer, position);
        // 从当前位置读取4kb数据
        fileChannel.read(buffer);
    }

    /**
     * @author shudj
     * @date 15:30 2019/3/8
     * @description 随机写
     *
     * @param
     * @return void
     **/
    public void random_writing(String path) throws FileNotFoundException {
        FileChannel fileChannel = new RandomAccessFile(new File(path), "rw").getChannel();
        ExecutorService executor = Executors.newFixedThreadPool(64);
        AtomicLong writePosition = new AtomicLong(0);
        for (int i = 0; i < 1024; i++) {
            final int index = i;
            executor.execute(() -> {
                try {
                    fileChannel.write(ByteBuffer.wrap(new byte[4 * 1024]),writePosition.getAndAdd(4 * 1024));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * @author shudj
     * @date 15:37 2019/3/8
     * @description 顺序写
     *
      * @param path
     * @return void
     **/
    public void sequential_write(String path) throws FileNotFoundException {
        FileChannel fileChannel = new RandomAccessFile(new File(path), "rw").getChannel();
        ExecutorService executor = Executors.newFixedThreadPool(64);
        AtomicLong writePosition = new AtomicLong(0);
        for (int i = 0; i < 1024; i++) {
            final int index = i;
            executor.execute(() -> {
                write(new byte[4 * 1024], fileChannel, writePosition);
            });
        }
    }

    private  synchronized void write(byte[] data, FileChannel fileChannel, AtomicLong writePosition) {
        try {
            fileChannel.write(ByteBuffer.wrap(data), writePosition.getAndAdd(4 *1024));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
