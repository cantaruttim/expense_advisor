package br.com.cantarutti.model.finance_data.dbs;

import com.mongodb.client.*;
import org.bson.Document;
import java.util.List;

public class MongoDBManager implements DatabaseManager {

    private final String uri;
    private final String databaseName;
    private final String collectionName;
    private MongoClient mongoClient;
    private MongoCollection<Document> collection;

    public MongoDBManager(String uri, String databaseName, String collectionName) {
        this.uri = uri;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    @Override
    public void connect() {
        this.mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        this.collection = database.getCollection(collectionName);
        System.out.println("✅ Connecting to MongoDB: " + databaseName + "/" + collectionName);
    }

    @Override
    public void insertDocuments(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            System.out.println("⚠️ No documents to insert.");
            return;
        }

        collection.insertMany(documents);
        System.out.println("✅ Inserted " + documents.size() + " documents on MongoDB!");
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("🔌 Close MongoDB connection.");
        }
    }
}
