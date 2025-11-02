package br.com.cantarutti.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.database")
public class DatabaseProperties {
    private String active;
    private Mongo mongodb;
    private Postgres postgresql;
    private Mysql mysql;

    public String getActive() { return active; }
    public void setActive(String active) { this.active = active; }

    public Mongo getMongodb() { return mongodb; }
    public void setMongodb(Mongo mongodb) { this.mongodb = mongodb; }

    public Postgres getPostgresql() { return postgresql; }
    public void setPostgresql(Postgres postgresql) { this.postgresql = postgresql; }

    public Mysql getMysql() { return mysql; }
    public void setMysql(Mysql mysql) { this.mysql = mysql; }

    // ---- MONGO ----
    public static class Mongo {
        private String uri;
        private String database;
        private String collection;

        public String getUri() { return uri; }
        public void setUri(String uri) { this.uri = uri; }

        public String getDatabase() { return database; }
        public void setDatabase(String database) { this.database = database; }

        public String getCollection() { return collection; }
        public void setCollection(String collection) { this.collection = collection; }
    }

    // ---- POSTGRES ----
    public static class Postgres {
        private String url;
        private String user;
        private String password;
        private String table;

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        public String getUser() { return user; }
        public void setUser(String user) { this.user = user; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getTable() { return table; }
        public void setTable(String table) { this.table = table; }
    }

    // ---- MYSQL ----
    public static class Mysql {
        private String url;
        private String user;
        private String password;
        private String table;

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        public String getUser() { return user; }
        public void setUser(String user) { this.user = user; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getTable() { return table; }
        public void setTable(String table) { this.table = table; }
    }
}
