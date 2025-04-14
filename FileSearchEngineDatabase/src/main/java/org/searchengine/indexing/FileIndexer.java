package org.searchengine.indexing;

import org.searchengine.database.DatabaseConnection;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.searchengine.util.SqlQueries.*;

public class FileIndexer {

    private static int directoryCount;
    private static int fileCount;

    public static void indexDirectory(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            indexFile(file);
        } else if (file.isDirectory()) {
            directoryCount++;
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    indexDirectory(subFile);
                }
            }
        }
    }

    private static void indexFile(File file) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(INSERT_FILES)) {

            fileCount++;
            byte[] rawBytes = Files.readAllBytes(file.toPath());
            String content = new String(rawBytes).replace("\u0000", "");

            BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

            statement.setString(1, file.getName());
            statement.setString(2, file.getAbsolutePath());
            statement.setString(3, content);
            statement.setLong(4, file.length());
            statement.setTimestamp(5, new Timestamp(attrs.lastModifiedTime().toMillis()));
            statement.executeUpdate();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearPreviousRecords() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(DELETE_FILES);
             PreparedStatement resetStmt = conn.prepareStatement(ALTER_FILES)) {

            deleteStmt.executeUpdate();
            resetStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "FileIndexer{" +
                "directoryCount=" + directoryCount +
                ", fileCount=" + fileCount +
                '}';
    }
}
