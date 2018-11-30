package com.jl.test.java;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;


/**
 * Author: shudj
 * Time: 2018/11/30 10:54
 * Description:
 */
public class ReadExcel {

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /**
     * @Author shudj
     * @Date 11:19 2018/11/30
     * @Description 判断Excel的版本。获取Workbook
     *
     * @param in
    * @param file
     * @return org.apache.poi.ss.usermodel.Workbook
     **/
    public static Workbook getWorkbok(InputStream in, File file) throws IOException {
        Workbook wb = null;

        // 2003
        if (file.getName().endsWith(EXCEL_XLS)) {
            wb = new HSSFWorkbook(in);
        // 2007
        } else if (file.getName().endsWith(EXCEL_XLSX)) {
            wb = new XSSFWorkbook(in);
        }

        return wb;
    }

    /**
     * @Author shudj
     * @Date 11:23 2018/11/30
     * @Description 检测文件是否是Excel
     *
     * @param file
     * @return void
     **/
    public static void checkExcelVaild(File file) throws Exception {
        if (!file.exists()) {
            throw new Exception("文件不存在");
        }

        if (!(file.isFile() &&
                (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))) {
            throw new Exception("文件不是Excel");
        }
    }

    public static Object getValue(Cell cell) {
        Object obj = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                obj = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                obj = cell.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_FORMULA:
                obj = cell.getCellFormula();
                break;
            case Cell.CELL_TYPE_ERROR:
                obj = cell.getErrorCellValue();
                break;
            default:
                obj = "--";
        }

        return obj;
    }

    public static void readExcel(String path) {

        File excelFile = new File(path);
        try (FileInputStream in = new FileInputStream(excelFile)) {
            checkExcelVaild(excelFile);
            Workbook wb = getWorkbok(in, excelFile);
            // sheet数量
            int sheetCount = wb.getNumberOfSheets();
            /**
             * 获取exce的sheet：从0开始
             */
            Sheet sheet = wb.getSheetAt(0);

            // 为跳过第一行目录设置count
            int count = 0;
            for (Row row: sheet) {
                // 跳过第一和第二行的目录
                if (count < 2) {
                    count ++;
                    continue;
                }

                // 如果当前行没有数据跳出循环
                if ("".equals(String.valueOf(row.getCell(0)))) {
                    return;
                }

                // 获取总列数(空格的不计算)
                int columnTotalNum = row.getPhysicalNumberOfCells();
                System.out.println("总列数：" + columnTotalNum);
                System.out.println("最大列数：" + row.getLastCellNum());

                int end = row.getLastCellNum();
                for (int i = 0; i < end; i++) {
                    Cell cell = row.getCell(i);
                    if (null == cell) {
                        System.out.println("null" + "\t");
                        continue;
                    }
                    Object obj = getValue(cell);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
