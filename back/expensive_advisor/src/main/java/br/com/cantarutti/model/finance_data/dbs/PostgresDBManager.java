package br.com.cantarutti.model.finance_data.dbs;

import br.com.cantarutti.model.finance_data.dbs.DatabaseManager;
import org.bson.Document;
import java.sql.*;
import java.util.List;
import java.util.Set;

public class PostgresDBManager implements DatabaseManager {

    private final String url;
    private final String user;
    private final String password;
    private final String tableName;
    private Connection connection;

    public PostgresDBManager(String url, String user, String password, String tableName) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.tableName = tableName;
    }

    @Override
    public void connect() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connecting to PostgreSQL!");
        } catch (SQLException e) {
            throw new RuntimeException("Error while connecting to PostgreSQL", e);
        }
    }

    @Override
    public void insertDocuments(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            System.out.println("⚠️ No documents to be inserted on PostgreSQL.");
            return;
        }

        try {
            connection.setAutoCommit(false);

            for (Document doc : documents) {
                Set<String> keys = doc.keySet();
                String columns = String.join(", ", keys);
                String placeholders = String.join(", ", keys.stream().map(k -> "?").toList());

                String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    int index = 1;
                    for (String key : keys) {
                        Object value = doc.get(key);
                        stmt.setObject(index++, value);
                    }
                    stmt.executeUpdate();
                }
            }

            connection.commit();
            System.out.println("✅ Inserted " + documents.size() + " rows on PostgreSQL!");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Error on rollback", ex);
            }
            throw new RuntimeException("Error while inserting data on PostgreSQL", e);
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("🔌 Closed PostgreSQL connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
