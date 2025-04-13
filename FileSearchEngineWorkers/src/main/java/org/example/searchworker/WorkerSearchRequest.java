package org.example.searchworker;

public class WorkerSearchRequest {
    private String query;

    public WorkerSearchRequest() {}

    public WorkerSearchRequest(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
