package br.com.cantarutti.model.finance_data.dbs;

import org.bson.Document;
import java.util.List;

public interface DatabaseManager extends AutoCloseable {

    void connect();
    void insertDocuments(List<Document> documents);

    @Override
    void close();
}
