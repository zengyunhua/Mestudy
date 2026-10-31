package com.tecsun.sisp.npm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private Database() {
    }

    public static Connection open() throws SQLException {
        String driver = AppConfig.get("jdbc.driver", "oracle.jdbc.driver.OracleDriver");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC driver is not available: " + driver, e);
        }
        return DriverManager.getConnection(
                AppConfig.required("jdbc.url"),
                AppConfig.required("jdbc.username"),
                AppConfig.required("jdbc.password"));
    }
}
