package org.example.searchmaster;

public class MasterSearchRequest {
    private String query;

    public MasterSearchRequest(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
