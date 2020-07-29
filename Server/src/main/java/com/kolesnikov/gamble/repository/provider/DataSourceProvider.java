package com.kolesnikov.gamble.repository.provider;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Properties;

public class DataSourceProvider {
    private final Properties properties;
    private final DataSource dataSource;

    public DataSourceProvider(Properties properties) {
        this.properties = properties;
        dataSource = new HikariDataSource(new HikariConfig(properties));
    }

    public DataSource getDataSource() {
        return dataSource;
    }

}
