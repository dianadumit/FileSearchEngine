package org.searchengine.search;

import org.searchengine.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.searchengine.util.SqlQueries.SEARCH_FILES;

public class FileSearchService {

    public static String searchFiles(String query) {
        StringBuilder results = new StringBuilder();
        String searchPattern = "%" + query + "%";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(SEARCH_FILES)) {

            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String filename = resultSet.getString("filename");
                    String filepath = resultSet.getString("filepath");
                    String contentPreview = resultSet.getString("content");
                    if (contentPreview.length() > 100) {
                        contentPreview = contentPreview.substring(0, 100) + "...";
                    }
                    results.append("File: ").append(filename).append("\n")
                            .append("Path: ").append(filepath).append("\n")
                            .append("Preview: ").append(contentPreview).append("\n\n");
                }
            }
        } catch (SQLException e) {
            results.append("Error: Unable to fetch results.\n");
            e.printStackTrace();
        }
        return results.length() > 0 ? results.toString() : "No results found.";
    }
}
