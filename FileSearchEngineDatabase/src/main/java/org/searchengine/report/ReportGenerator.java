package org.searchengine.report;

import java.io.IOException;

public interface ReportGenerator {

  String writeReport(ReportData data) throws IOException;
}
