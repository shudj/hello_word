package com.example.tool.mode.decorate;

/**
 * zhuang shi she ji mo shi
 * te dian:
 *  ou he xing bu qiang, bei zhuang shi de lei de bian hua yu zhuang shi lei de bian hua wu guan
 */
public class Decorate {

    public static void main(String[] args) {
        new DecorateStu(new Student()).code();
    }
}


interface Coder {
    public void code();
}

class Student implements Coder {

    @Override
    public void code() {
        System.out.println("java");
    }
}

class DecorateStu implements Coder {

    private Student stu;

    public DecorateStu(Student stu) {
        this.stu = stu;
    }

    @Override
    public void code() {
        stu.code();
        System.out.println("decorate java");
    }
}