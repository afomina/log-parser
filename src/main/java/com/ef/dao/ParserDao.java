package com.ef.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Base dao class
 */
public abstract class ParserDao {

    private static final String CONNECTION_PROPERTIES_FILE_NAME = "connection.properties";
    private Properties properties;

    ParserDao() {
        properties = new Properties();
        InputStream config = ParserDao.class.getResourceAsStream(CONNECTION_PROPERTIES_FILE_NAME);
        try {
            properties.load(config);
        } catch (IOException e) {
            System.out.println("unable to load database connection properties");
        }
    }

    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(properties.getProperty("connection.url"),
                properties.getProperty("dbuser"),
                properties.getProperty("dbpass"));
    }

}
