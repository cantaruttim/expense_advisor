package br.com.cantarutti.config;

import br.com.cantarutti.model.finance_data.dbs.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseFactory {

    @Bean
    public DatabaseManager databaseManager(DatabaseProperties props) {
        String active = props.getActive().toLowerCase();

        return switch (active) {
            case "mongodb" -> new MongoDBManager(
                    props.getMongodb().getUri(),
                    props.getMongodb().getDatabase(),
                    props.getMongodb().getCollection()
            );

            case "postgresql" -> new PostgresDBManager(
                    props.getPostgresql().getUrl(),
                    props.getPostgresql().getUser(),
                    props.getPostgresql().getPassword(),
                    props.getPostgresql().getTable()
            );

            case "mysql" -> new MySQLDBManager(
                    props.getMysql().getUrl(),
                    props.getMysql().getUser(),
                    props.getMysql().getPassword(),
                    props.getMysql().getTable()
            );

            default -> throw new IllegalArgumentException("Database not supported: " + active);
        };
    }
}
