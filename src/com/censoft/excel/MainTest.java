package com.censoft.excel;

import java.util.List;
import java.util.Map;

import com.censoft.excel.mypoi.ExcelReader;

public class MainTest {
    public static void main(String[] args) {
        // 设定Excel文件所在路径
        String excelFileName = "C:/Users/Administrator/Desktop/请产业经济科协助查询税收及营收数据-0803.xlsx";
        // 读取Excel文件内容
        List<Map<String, Object>> readResult = ExcelReader.readExcel(excelFileName);
        System.out.println(readResult);
    }
}
