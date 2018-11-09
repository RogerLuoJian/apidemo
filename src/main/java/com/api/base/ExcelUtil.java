package com.api.base;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtil {
    public static XSSFSheet ExcelSheet;
    public static XSSFWorkbook ExcelBook;
    public static XSSFRow Row;
    public static XSSFCell Cell;
    private static final Logger logger = Logger.getLogger(ExcelUtil.class.getName());

    public static void setExcelFile(String Path) throws IOException {
        FileInputStream ExcelFile;
        try {
            ExcelFile = new FileInputStream(Path);
            ExcelBook = new XSSFWorkbook(ExcelFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw e;
        }
    }

    public static String getCellData(int RowNum, int ColNum, String SheetName) {
        String cellData = null;
        ExcelSheet = ExcelBook.getSheet(SheetName);
        Cell = ExcelSheet.getRow(RowNum).getCell(ColNum);
        if (Cell == null) {
            cellData = "";
        } else {
            cellData = Cell.getStringCellValue();


        }
        logger.debug(String.format("Sheet: %s, row - %s, column - %s with value: %s", SheetName, RowNum, ColNum, cellData));
        return cellData;

    }

    public static int getLastRowNums(String SheetName) throws Exception {
        try {
            ExcelSheet = ExcelBook.getSheet(SheetName);
            int rowCount = ExcelSheet.getLastRowNum();
            return rowCount;
        } catch (Exception e) {
            throw (e);
        }
    }

    public static int getLastColumNums(String SheetName) throws Exception {
        try {
            ExcelSheet = ExcelBook.getSheet(SheetName);
            int columnCount = ExcelSheet.getRow(0).getPhysicalNumberOfCells();
            return columnCount;
        } catch (Exception e) {
            throw (e);
        }
    }
}
