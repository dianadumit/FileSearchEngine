package iteration1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection databaseLink;

    public static Connection getConnection() throws SQLException {
        String databaseUser = "postgres";
        String databasePassword = "1234";
        String url = "jdbc:postgresql://localhost:5432/searchEngine";

        try {
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
}
