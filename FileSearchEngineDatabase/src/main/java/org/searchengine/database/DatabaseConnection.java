package org.searchengine.database;

import java.io.*;
import java.sql.*;
import java.util.*;

public class DatabaseConnection {

  private static final String PROPERTIES_FILE = "db.properties";

  public static Connection getConnection() throws SQLException {
    Properties props = new Properties();

    try (InputStream input = DatabaseConnection.class.getClassLoader()
        .getResourceAsStream(PROPERTIES_FILE)) {
      if (input == null) {
        throw new RuntimeException("Unable to find " + PROPERTIES_FILE);
      }
      props.load(input);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load database properties", e);
    }

    String url = props.getProperty("db.url");
    String user = props.getProperty("db.user");
    String password = props.getProperty("db.password");

    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
    }

    return DriverManager.getConnection(url, user, password);
  }
}
