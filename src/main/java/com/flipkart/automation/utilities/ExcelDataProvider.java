package com.flipkart.automation.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Reads Excel test data and exposes it as TestNG-compatible 2D Object arrays.
 */
public final class ExcelDataProvider {

    private static final Logger logger = LogManager.getLogger(ExcelDataProvider.class);
    private static final DataFormatter DATA_FORMATTER = new DataFormatter();

    private ExcelDataProvider() {
    }

    /**
     * Reads Excel from classpath (works in Eclipse and Maven regardless of working directory).
     *
     * @param classpathResource e.g. "testdata/credentials.xlsx"
     * @param sheetName         sheet to read
     */
    public static Object[][] readExcelFromClasspath(String classpathResource, String sheetName) {
        logger.info("Reading Excel from classpath: {} | sheet: {}", classpathResource, sheetName);
        InputStream inputStream = ExcelDataProvider.class.getClassLoader().getResourceAsStream(classpathResource);
        if (inputStream == null) {
            throw new RuntimeException("Excel resource not found on classpath: " + classpathResource
                    + ". Run scripts/generate-testdata.ps1 to create test data files.");
        }
        try (InputStream is = inputStream; Workbook workbook = WorkbookFactory.create(is)) {
            return parseSheet(workbook, sheetName, classpathResource);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel from classpath: " + classpathResource, e);
        }
    }

    /**
     * Reads all data rows from a filesystem path (fallback for local file access).
     */
    public static Object[][] readExcelFile(String filePath, String sheetName) {
        logger.info("Reading Excel file: {} | sheet: {}", filePath, sheetName);
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            return parseSheet(workbook, sheetName, filePath);
        } catch (IOException e) {
            logger.error("Failed to read Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
    }

    public static Object[][] readExcelFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            return readExcelFile(filePath, workbook.getSheetName(0));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
    }

    private static Object[][] parseSheet(Workbook workbook, String sheetName, String source) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new RuntimeException("Sheet '" + sheetName + "' not found in: " + source);
        }

        Iterator<Row> rowIterator = sheet.iterator();
        if (!rowIterator.hasNext()) {
            return new Object[0][0];
        }

        rowIterator.next();
        List<Object[]> dataRows = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (!isRowEmpty(row)) {
                dataRows.add(readRowAsArray(row));
            }
        }

        Object[][] data = dataRows.toArray(new Object[0][]);
        logger.info("Loaded {} rows from sheet '{}'", data.length, sheetName);
        return data;
    }

    private static Object[] readRowAsArray(Row row) {
        int lastCellNum = row.getLastCellNum();
        Object[] rowData = new Object[lastCellNum];
        for (int i = 0; i < lastCellNum; i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            rowData[i] = cell == null ? "" : DATA_FORMATTER.formatCellValue(cell);
        }
        return rowData;
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !DATA_FORMATTER.formatCellValue(cell).isBlank()) {
                return false;
            }
        }
        return true;
    }
}
