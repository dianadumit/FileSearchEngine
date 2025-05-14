package org.searchengine.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Paths;

public class JSONReportGenerator implements ReportGenerator {

  private final ObjectMapper mapper;

  public JSONReportGenerator() {
    mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(SerializationFeature.INDENT_OUTPUT);
  }

  @Override
  public String writeReport(ReportData data) throws IOException {
    String fileName = "index_report.json";
    mapper.writeValue(Paths.get(fileName).toFile(), data);
    return Paths.get(fileName).toAbsolutePath().toString();
  }
}
