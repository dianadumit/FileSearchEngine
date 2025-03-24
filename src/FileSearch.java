import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileSearch {
    public static String getResult(String sqlQuery) {
        StringBuilder results = new StringBuilder();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT filename, filepath, content FROM files " +
                    "WHERE filename ILIKE ? OR filepath ILIKE ? OR content ILIKE ? ";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                String searchPattern = "%" + sqlQuery + "%";
                statement.setString(1, searchPattern);
                statement.setString(2, searchPattern);
                statement.setString(3, searchPattern);

                try (ResultSet result = statement.executeQuery()) {
                    while (result.next()) {
                        String filename = result.getString("filename");
                        String filepath = result.getString("filepath");
                        String contentPreview = result.getString("content");

                        if (contentPreview.length() > 100) {
                            contentPreview = contentPreview.substring(0, 100) + "...";
                        }

                        results.append("File: " + filename + "\n")
                                .append("Path: " + filepath + "\n")
                                .append("Preview: " + contentPreview + "\n\n");
                    }
                }
            }
        } catch (SQLException e) {
            results.append("Error: Unable to fetch results.\n");
            e.printStackTrace();
        }
        return results.length() > 0 ? results.toString() : "No results found.";
    }
}
