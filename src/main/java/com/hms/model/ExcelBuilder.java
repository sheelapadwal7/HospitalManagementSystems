
package com.hms.model;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



import java.io.*;
import java.util.Date;

@Component
public class ExcelBuilder {

    private static final String FILE_NAME = "generated_excel.xlsx";

    @Scheduled(fixedRate = 60000) // runs every minute, adjust as needed
    public void buildExcel() {
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Data");

        Row headerRow = sheet.createRow(0);
        String[] columns = { "ID", "Name", "Date" };
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(1);
        dataRow.createCell(1).setCellValue("John Doe");
        dataRow.createCell(2).setCellValue(createHelper.createRichTextString(new Date().toString()));

        try {
            FileOutputStream fileOut = new FileOutputStream(FILE_NAME);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

