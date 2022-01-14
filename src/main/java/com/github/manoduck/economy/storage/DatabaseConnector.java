package com.github.manoduck.economy.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnector {

    public HikariDataSource dataSource;

    public DatabaseConnector(String url, String username, String password) {

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(hikariConfig);

        createTables();

    }

    private void createTables() {

        try  {

            PreparedStatement userStatement = dataSource.getConnection().prepareStatement("create table if not exists economy_users (" +
                    "id int auto_increment primary key," +
                    "name varchar(16) not null," +
                    "balance double not null," +
                    "constraint economy_users_uindex unique (id)," +
                    "constraint economy_users_name_uindex unique (name)" +
                    ")" +
                    "engine=InnoDB;");

            userStatement.execute();
            userStatement.close();

        }

        catch (SQLException exception) { exception.printStackTrace(); }

    }
}