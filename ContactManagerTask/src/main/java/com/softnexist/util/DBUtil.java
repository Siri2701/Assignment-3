package com.softnexist.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class DBUtil {
    private static HikariDataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            try {
                HikariConfig config = new HikariConfig();

                // 1. Point directly to your local MySQL server
                config.setJdbcUrl("jdbc:mysql://localhost:3306/contactdb?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC");
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");

                // 2. Supply your real database credentials
                config.setUsername("root");
                config.setPassword("Anusha@2006"); // <-- Change this to your real MySQL password!

                // 3. Set performance pool constraints
                config.setMaximumPoolSize(20);
                config.setConnectionTimeout(30000);
                config.setIdleTimeout(600000);

                dataSource = new HikariDataSource(config);
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize local HikariCP connection pool.", e);
            }
        }
        return dataSource;
    }
}