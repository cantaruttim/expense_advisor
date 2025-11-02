package br.com.cantarutti;

import br.com.cantarutti.config.DatabaseProperties;
import br.com.cantarutti.model.finance_data.ExcelExpensesReader;
import br.com.cantarutti.model.finance_data.dbs.DatabaseManager;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ExpensiveAdvisorApplication implements CommandLineRunner {

    @Autowired
    private DatabaseManager databaseManager;

    @Autowired
    private DatabaseProperties props;

    @Autowired
    private ExcelExpensesReader excelReader;

    public static void main(String[] args) {
        SpringApplication.run(ExpensiveAdvisorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Starting import to database: " + props.getActive().toUpperCase());

        List<Document> docs = excelReader.readExcelFile(
                "/Users/matheuscantarutti/Desktop/Personal/Finance/calc_financeira/dados/parcelado.xlsx"
        );

        databaseManager.connect();
        databaseManager.insertDocuments(docs);
        databaseManager.close();

        System.out.println("✅ Import finished successfully!");
    }
}
