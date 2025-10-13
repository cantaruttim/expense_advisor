package br.com.cantarutti.model.finance_data;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelToMongoDB {
    public static void main(String[] args) {
        String excelFilePath = "/Users/matheuscantarutti/Desktop/Personal/Finance/calc_financeira/dados/expenses_java_version.xlsx";
        String mongoUri = "mongodb://localhost:27017";
        String databaseName = "expensive_adivisor";
        String collectionName = "expenses";

        try (
                MongoClient mongoClient = MongoClients.create(mongoUri);
                FileInputStream fis = new FileInputStream(excelFilePath);
                Workbook workbook = new XSSFWorkbook(fis)
        ) {

            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            Sheet sheet = workbook.getSheetAt(0); // sheet at position 0
            Iterator<Row> rowIterator = sheet.iterator();

            List<String> headers = new ArrayList<>();
            List<Document> documents = new ArrayList<>();

            // read the first line (header)
            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();
                headerRow.forEach(cell -> headers.add(cell.getStringCellValue()));
            }

            // read data values of
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

            // Insert data values on Mongo
            if (!documents.isEmpty()) {
                collection.insertMany(documents);
                System.out.println("data inserted sucessufly: " + documents.size() + " documents.");
            } else {
                System.out.println("None data to be inserted!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
