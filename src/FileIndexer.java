import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FileIndexer {
    private static int numberOfDirectories;
    private static int numberOfFiles;

    @Override
    public String toString() {
        return "FoundFiles{" +
                "numberOfDirectories=" + numberOfDirectories +
                ", numberOfFiles=" + numberOfFiles +
                '}';
    }

    public static void clearPreviousRecords() {
        try (Connection databaseConnection = DatabaseConnection.getConnection()) {
            String deleteQuery = "DELETE FROM files";
            PreparedStatement deleteStatement = databaseConnection.prepareStatement(deleteQuery);
            deleteStatement.executeUpdate();

            String resetQuery = "ALTER SEQUENCE files_id_seq RESTART WITH 1";
            PreparedStatement resetStatement = databaseConnection.prepareStatement(resetQuery);
            resetStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void indexFile(File file) {

        try (Connection databaseConnection = DatabaseConnection.getConnection()) {
            String sqlQuery = "INSERT INTO files (filename, filepath, content, size, last_modified) VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT (filepath) DO NOTHING";
            PreparedStatement statement = databaseConnection.prepareStatement(sqlQuery);

            if (file.isFile()) {
//this is better if reading big files
//                try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        System.out.println(line);
//                    }
//                } catch (
//                        FileNotFoundException e) {
//                    System.out.println("Cannot find the file:");
//                    System.out.println(file.getAbsolutePath());
//                } catch (
//                        IOException e) {
//                    System.out.println("There was an error during the process of locating the file");
//                }

                numberOfFiles++;
                String content = new String(Files.readAllBytes(file.toPath()));
                BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                statement.setString(1, file.getName());
                statement.setString(2, file.getAbsolutePath());
                statement.setString(3, content);
                statement.setLong(4, file.length());
                statement.setTimestamp(5, new Timestamp(attrs.lastModifiedTime().toMillis()));
                statement.executeUpdate();
            } else {
                numberOfDirectories++;
                File[] files = file.listFiles();
                for (File file1 : files) {
                    indexFile(file1);
                }

                statement.executeBatch();
                System.out.println("Indexing completed.");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
