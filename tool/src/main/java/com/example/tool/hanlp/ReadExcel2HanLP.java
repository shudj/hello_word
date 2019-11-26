package com.example.tool.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.io.File;
import java.util.List;

/**
 * @author: shudj
 * @time: 2019/5/27 9:57
 * @description:
 */
public class ReadExcel2HanLP {

    public static void main(String[] args) {
        ReadExcel.readExcel("C:\\Users\\admin\\Desktop\\area.xlsx");
    }

    /**
     * @author shudj
     * @date 10:07 2019/5/27
     * @description 获取城市的名称，市或者区
     *
      * @param str
     * @return java.lang.String
     **/
    public static String getCity(String str) {
        List<Term> area = getArea(str);
        return null;
    }

    /**
     * @author shudj
     * @date 10:05 2019/5/27
     * @description 解析地名
     *
      * @param str 传入的地址字符串
     * @return java.util.List<com.hankcs.hanlp.seg.common.Term>
     **/
    public static List<Term> getArea(String str) {

        Segment areaSegment = HanLP.newSegment().enablePlaceRecognize(true);
        return areaSegment.seg(str);
    }
}
