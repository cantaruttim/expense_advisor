package br.com.cantarutti.model.finance_data;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelExpensesReader {

    public List<Document> readExcelFile(String filePath) throws IOException {
        List<Document> documents = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (!rowIterator.hasNext()) {
                System.out.println("⚠️ Empty file!");
                return documents;
            }

            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            headerRow.forEach(cell -> headers.add(cell.getStringCellValue()));

            System.out.println("Headers encontrados: " + headers);
            System.out.println("Total number os rows: " + (sheet.getPhysicalNumberOfRows() - 1));

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Document doc = new Document();

                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cell.getCellType()) {
                        case STRING -> doc.append(headers.get(i), cell.getStringCellValue());
                        case NUMERIC -> doc.append(headers.get(i), cell.getNumericCellValue());
                        case BOOLEAN -> doc.append(headers.get(i), cell.getBooleanCellValue());
                        case BLANK -> doc.append(headers.get(i), null);
                        default -> doc.append(headers.get(i), cell.toString());
                    }
                }
                documents.add(doc);
            }
        }
        System.out.println("Document is loaded correctly!");
        return documents;
    }
}
