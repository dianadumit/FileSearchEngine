package org.searchengine.report;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextReportGenerator implements ReportGenerator {
    @Override
    public String writeReport(ReportData data) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Index Report\n");
        sb.append("Start Time      : ").append(data.getStartTime()).append("\n");
        sb.append("End Time        : ").append(data.getEndTime()).append("\n");
        sb.append("Total Duration  : ").append(data.getDuration().toMillis()/1000).append(" seconds\n");
        sb.append("Indexed Files (" + data.getIndexedPaths().size() + "):\n");
        for (String path : data.getIndexedPaths()) {
            sb.append("  " + path + "\n");
        }
        sb.append("\n");

        sb.append("Skipped Files (" + data.getSkippedPaths().size() + "):\n");
        for (String path : data.getSkippedPaths()) {
            sb.append("  " + path + "\n");
        }
        sb.append("\n");

        sb.append("Failed Files (" + data.getFailedPaths().size() + "):\n");
        for (String path : data.getFailedPaths()) {
            sb.append("  " + path + "\n");
        }

        Path out = Paths.get("index_report.txt");
        Files.write(out, sb.toString().getBytes(StandardCharsets.UTF_8));
        return out.toAbsolutePath().toString();
    }
}
