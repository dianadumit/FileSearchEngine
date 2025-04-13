package org.example.searchworker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
public class SearchService {

    @Value("${worker.folder.path}")
    private String folderPath;

    public List<String> performSearch(String query) {
        TreeMap<Integer, List<String>> sortedResults = new TreeMap<>();
        File dir = new File(folderPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return List.of("Directory not found or invalid: " + folderPath);
        }
        recursiveSearch(dir, query, sortedResults);

        List<String> finalResults = new ArrayList<>();
        System.out.println("Sorted Results Map: " + sortedResults);

        for (List<String> group : sortedResults.descendingMap().values()) {
            finalResults.addAll(group);
        }
        return finalResults;
    }

    private void recursiveSearch(File file, String query, TreeMap<Integer, List<String>> results) {
        if (file.isFile()) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                int frequencyScore = countOccurrences(content.toLowerCase(), query.toLowerCase());
                if (frequencyScore > 0) {
                    String resultEntry = file.getName()
                            + " (path: " + file.getAbsolutePath()
                            + " appears " + frequencyScore + " times)";
                    results.computeIfAbsent(frequencyScore, k -> new ArrayList<>()).add(resultEntry);
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

    private int countOccurrences(String content, String query) {
        if (content == null || query == null || query.isEmpty()) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = content.indexOf(query, index)) != -1) {
            count++;
            index += query.length();
        }
        return count;
    }
}