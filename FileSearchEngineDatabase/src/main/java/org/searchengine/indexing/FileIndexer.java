package org.searchengine.indexing;

import org.searchengine.database.DatabaseConnection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.searchengine.util.SqlQueries.*;
public class FileIndexer {

    private static final Set<String> TEXT_EXTENSIONS = new HashSet<>(Arrays.asList(
            "txt", "md", "doc", "docx", "java", "xml", "json", "csv", "html", "css", "js", "pl"
    ));
    private static final int FLUSH_THRESHOLD = 500;

    private static int directoryCount = 0;
    private static int fileCount = 0;

    public static void indexDirectory(File root) {
        if (root == null || !root.exists()) return;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(INSERT_FILES)) {
                traverse(root, statement, connection);
                statement.executeBatch();
                connection.commit();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        System.out.printf("Indexed %d files in %d directories.%n", fileCount, directoryCount);
    }

    private static void traverse(File file, PreparedStatement statement, Connection connection) throws IOException, SQLException {
        if (file.isDirectory()) {
            directoryCount++;
            File[] dirs = file.listFiles();
            if (dirs != null) {
                for (File subDir : dirs) {
                    traverse(subDir, statement, connection);
                }
            }
        } else if (file.isFile()) {
            String ext = getExtension(file).toLowerCase();
            long size = file.length();

            if (!TEXT_EXTENSIONS.contains(ext) || size > 10000000) {
                return;
            }

            indexFile(file, statement);
            if (fileCount % FLUSH_THRESHOLD == 0) {
                statement.executeBatch();
                connection.commit();
            }
        }
    }

    private static void indexFile(File file, PreparedStatement statement) throws IOException, SQLException {
        fileCount++;

        byte[] rawBytes = Files.readAllBytes(file.toPath());
        String content = new String(rawBytes).replace("\u0000", "");
        BasicFileAttributes attrs = Files.readAttributes(
                file.toPath(), BasicFileAttributes.class);

        double score = computeScore(file, attrs);

        statement.setString(1, file.getName());
        statement.setString(2, file.getAbsolutePath());
        statement.setString(3, content);
        statement.setLong(4, file.length());
        statement.setTimestamp(5, new Timestamp(attrs.lastModifiedTime().toMillis()));
        statement.setDouble(6, score);
        statement.addBatch();
    }

    private static String getExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex <= 0 || dotIndex == name.length() - 1) {
            return "";
        }
        return name.substring(dotIndex + 1);
    }


    private static double computeScore(File file, BasicFileAttributes attributes) {
        double score = 0;
        String path = file.getAbsolutePath();
        score += 1.0 / (path.length() + 1);

        String ext = getExtension(file);
        switch (ext) {
            case "txt":
                score += 2.0;
                break;
            case "java":
                score += 1.0;
                break;
            case "md":
                score += 0.5;
                break;
            default:
                score += 0.4;
                break;
        }

        long ageMs = System.currentTimeMillis() - attributes.lastModifiedTime().toMillis();
        double ageDays = ageMs / (1000.0 * 60 * 60 * 24);
        score += Math.max(0, 1.0 - ageDays / 365);

        int depth = path.split(File.separator.equals("\\") ? "\\\\" : File.separator).length;
        score += 1.0 / (depth + 1);

        long size = file.length();
        if (size < 1024) score -= 0.5;
        else if (size > 10000000) score -= 0.2;

        return score;
    }

    public static void clearPreviousRecords() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_FILES);
             PreparedStatement restartStatement = connection.prepareStatement(ALTER_FILES)) {
            deleteStatement.executeUpdate();
            restartStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
