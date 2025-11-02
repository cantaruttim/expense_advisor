package br.com.cantarutti.model.finance_data;

import com.mongodb.client.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExpensesExcelToDatabase {

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

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (!rowIterator.hasNext()) {
                System.out.println("⚠️ Planilha vazia!");
                return;
            }

            // Lê o cabeçalho de forma genérica
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            headerRow.forEach(cell -> headers.add(cell.getStringCellValue()));
            System.out.println("Headers encontrados: " + headers);

            int totalRows = sheet.getPhysicalNumberOfRows() - 1; // subtrai o header
            System.out.println("Total de linhas de dados: " + totalRows);

            int insertedCount = 0;

            // Itera pelas linhas de dados
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

                collection.insertOne(doc);
                insertedCount++;
            }

            System.out.println("✅ Inserção concluída!");
            System.out.println("Total de documentos inseridos: " + insertedCount);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
