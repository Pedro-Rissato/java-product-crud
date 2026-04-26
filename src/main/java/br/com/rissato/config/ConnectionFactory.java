package br.com.rissato.config;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionFactory {

    private final HikariDataSource dataSource;

    public ConnectionFactory(DbConfig dbConfig) {
        if (dbConfig == null) {
            throw new IllegalArgumentException("Database config cannot be null.");
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(dbConfig.url());
        config.setUsername(dbConfig.user());
        config.setPassword(dbConfig.password());
        config.setPoolName("product-crud-pool");
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000);
        config.setValidationTimeout(5000);
        config.setMaxLifetime(1800000);

        this.dataSource = new HikariDataSource(config);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void shutdown() {
        if (!dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public static ConnectionFactory create() {
        DbConfig dbConfig = DbConfig.load();
        return new ConnectionFactory(dbConfig);
    }
} 