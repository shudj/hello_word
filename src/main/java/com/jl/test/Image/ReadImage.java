package com.jl.test.Image;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

import java.io.File;

/**
 * 读取图片中的内容
 * @author shudj
 */
public class ReadImage {

    public static void main(String[] agrs) throws TesseractException {

        File file = new File("C:\\Users\\admin\\Desktop\\image");
        Tesseract instance = new Tesseract();
        //File tessDataFolder = LoadLibs.extractTessResources("testdata");
        //Set the tessdata path
        //instance.setDatapath("D:/Soft/Tesseract-OCR/tessdata");
        instance.setDatapath("C:/Program Files (x86)/Tesseract-OCR/tessdata");
        instance.setLanguage("eng");
        instance.setLanguage("chi_sim");
        File[] files = file.listFiles();
        for (File image: files) {
            String result = instance.doOCR(image);
            System.out.println(result);
            image.delete();
        }


    }
}
