package org.searchengine.search;

import org.searchengine.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.searchengine.util.SqlQueries.SEARCH_FILES;

public class FileSearchService {

    public static String searchFiles(String query) {
        Map<String, List<String>> queryParts = parseQuery(query);
        StringBuilder sqlBuilder = new StringBuilder("SELECT filename, filepath, content FROM files WHERE ");

        List<String> conditions = new ArrayList<>();
        List<String> parameters = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : queryParts.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            if ("path".equalsIgnoreCase(key)) {
                for (String val : values) {
                    if (val.matches("(?i)^[a-zA-Z]:\\\\.*")) {
                        if (val.endsWith("\\") || val.endsWith("/")) {
                            conditions.add("filepath LIKE ?");
                            parameters.add(val.replace("\\", "\\\\") + "%");
                        } else {
                            conditions.add("filepath = ?");
                            parameters.add(val);
                        }
                    } else {
                        conditions.add("filepath ILIKE ?");
                        parameters.add("%" + val + "%");
                    }
                }

            } else if ("filename".equalsIgnoreCase(key) || "name".equalsIgnoreCase(key)) {
                for (String val : values) {
                    conditions.add("filename ILIKE ?");
                    parameters.add("%" + val + "%");
                }

            } else if ("content".equalsIgnoreCase(key)) {
                for (String val : values) {
                    conditions.add("content ILIKE ?");
                    parameters.add("%" + val + "%");
                }
            }
        }

        if (conditions.isEmpty()) {
            return "Invalid query. Please use qualifiers like: path:, content:, filename:";
        }

        sqlBuilder.append(String.join(" AND ", conditions));
        sqlBuilder.append(" ORDER BY score DESC");

        System.out.println("Generated SQL: " + sqlBuilder);
        System.out.println("Parameters: " + parameters);

        StringBuilder results = new StringBuilder();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setString(i + 1, parameters.get(i));
            }
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

    private static Map<String, List<String>> parseQuery(String input) {
        Map<String, List<String>> parsed = new HashMap<>();
        String[] parts = input.trim().split("\\s+");

        for (String part : parts) {
            String[] keyValue = part.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().toLowerCase();
                String value = keyValue[1].trim();
                parsed.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            }
        }

        return parsed;
    }

}
