package com.scarlatti;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 11/17/2018
 */
public class XlsxConverter {

    public XlsxWorkbook convert(Path file) {

        XlsxWorkbook workbook1 = new XlsxWorkbook();

        try (Workbook workbook = new XSSFWorkbook(file.toFile())) {
            int numSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);

                // add a new sheet to the working model
                XlsxSheet newSheet = new XlsxSheet();
                workbook1.put(sheet.getSheetName(), newSheet);

                // convert the values in the sheet
                convertSheetWithHeaders(sheet, newSheet);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error converting workbook " + file, e);
        }

        return workbook1;
    }

    private void convertSheetWithHeaders(Sheet sheet, XlsxSheet xlsxSheet) {
        int firstRowNum = sheet.getFirstRowNum();  // row nums start with 1
        int lastRowNum = sheet.getLastRowNum();

        if (lastRowNum == 0) {
            return;
        }

        for (int i = firstRowNum + 1; i < lastRowNum + 1; i++) {
            Row row = sheet.getRow(i);

            if (row == null) {
                continue;  // if a row contains only empty cells it will be null
            }

            // stuff the record into the working sheet model
            XlsxRow xlsxRow = new XlsxRow();
            xlsxSheet.add(xlsxRow);

            Map<Integer, String> headerNames = getHeaderNames(sheet);

            // find the value for each header (if it exists)
            for (int j : headerNames.keySet()) {
                // Get string value of cell
                String stringCellValue = getStringCellValue(row.getCell(j));
                xlsxRow.put(headerNames.get(j), stringCellValue);
            }
        }
    }

    private Map<Integer, String> getHeaderNames(Sheet sheet) {
        Map<Integer, String> headerNames = new HashMap<>();

        Row row = sheet.getRow(0);
        int firstCellNum = row.getFirstCellNum();
        int lastCellNum = row.getLastCellNum();

        for (int i = firstCellNum; i < lastCellNum; i++) {
            Cell cell = row.getCell(i);
            headerNames.put(i, cell.getStringCellValue());
        }

        return headerNames;
    }

    private String getStringCellValue(Cell cell) {
        String stringCellValue = null;

        if (cell == null) {
            return "";
        }

        // Get cell type.
        CellType cellType = cell.getCellType();

        switch (cellType) {
            case NUMERIC:
                double numberValue = cell.getNumericCellValue();

                // BigDecimal is used to avoid double value is counted use Scientific counting method.
                // For example the original double variable value is 12345678, but jdk translated the value to 1.2345678E7.
                return BigDecimal.valueOf(numberValue).toPlainString();
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                boolean booleanValue = cell.getBooleanCellValue();
                return String.valueOf(booleanValue);
            case BLANK:
                return "";
            default:
                throw new RuntimeException("Not able to handle cell type " + cellType);
        }
    }
}
