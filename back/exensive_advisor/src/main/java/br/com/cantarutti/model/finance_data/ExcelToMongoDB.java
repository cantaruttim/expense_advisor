package br.com.cantarutti.model.finance_data;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

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

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            List<String> headers = new ArrayList<>();
            int insertedCount = 0;
            int skippedCount = 0;

            // Lê o cabeçalho
            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();
                headerRow.forEach(cell -> headers.add(cell.getStringCellValue()));
            }

            // Lê as linhas de dados e verifica duplicatas antes de inserir
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

                // 🔍 Verifica se já existe no MongoDB
                boolean exists = collection.find(
                        Filters.and(
                                Filters.eq("Date", doc.get("Date")),
                                Filters.eq("Description", doc.get("Description")),
                                Filters.eq("Amount", doc.get("Amount"))
                        )
                ).first() != null;

                if (!exists) {
                    collection.insertOne(doc);
                    insertedCount++;
                } else {
                    skippedCount++;
                }
            }

            System.out.println("✅ Inserção concluída!");
            System.out.println("Novos documentos inseridos: " + insertedCount);
            System.out.println("Documentos ignorados (duplicados): " + skippedCount);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
