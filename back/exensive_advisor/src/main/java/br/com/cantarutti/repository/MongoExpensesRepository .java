import com.mongodb.client.*;
import org.bson.Document;

import java.util.List;
import java.util.Map;

public class MongoExpensesRepository implements GenericExpensesRepository {
    private final String uri;
    private final String dbName;
    private final String collectionName;

    public MongoExpensesRepository(String uri, String dbName, String collectionName) {
        this.uri = uri;
        this.dbName = dbName;
        this.collectionName = collectionName;
    }

    @Override
    public void save(List<Map<String, Object>> data) {
        try (MongoClient client = MongoClients.create(uri)) {
            MongoDatabase database = client.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            for (Map<String, Object> row : data) {
                Document doc = new Document(row);
                collection.insertOne(doc);
            }

            System.out.println("✅ Inseridos " + data.size() + " documentos no MongoDB");
        }
    }
}
