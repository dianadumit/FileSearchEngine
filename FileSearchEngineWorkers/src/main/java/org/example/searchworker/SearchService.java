package org.example.searchworker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Value("${worker.folder.path}")
    private String folderPath;

    public List<String> performSearch(String query) {
        List<String> results = new ArrayList<>();

        File dir = new File(folderPath);
        if (!dir.exists() || !dir.isDirectory()) {
            results.add("Directory not found or invalid: " + folderPath);
            return results;
        }
        recursiveSearch(dir, query, results);
        return results;
    }

    private void recursiveSearch(File file, String query, List<String> results) {
        if (file.isFile()) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                if (file.getName().contains(query) || content.contains(query)) {
                    results.add(file.getName() + " (path: " + file.getAbsolutePath() + ")");
                }
            } catch (IOException e) {
                System.err.println("Error reading file " + file.getAbsolutePath());
                e.printStackTrace();
            }
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    recursiveSearch(child, query, results);
                }
            }
        }
    }
}
