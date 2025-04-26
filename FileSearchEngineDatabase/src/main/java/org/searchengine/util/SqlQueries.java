package org.searchengine.util;

public class SqlQueries {
    public static final String SEARCH_FILES =
            "SELECT filename, filepath, content FROM files " +
                    "WHERE filename ILIKE ? OR filepath ILIKE ? OR content ILIKE ?";
    public static final String INSERT_FILES = "INSERT INTO files (filename, filepath, content, size, last_modified, score) " +
            "VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (filepath) DO NOTHING";

    public static final String DELETE_FILES = "DELETE FROM files";
    public static final String ALTER_FILES = "ALTER SEQUENCE files_id_seq RESTART WITH 1";
}
