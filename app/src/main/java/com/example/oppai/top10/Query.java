package com.example.oppai.top10;

public class Query {
    private String query;
    private String sortBy;
    private String language;
    private String fromDate;
    private String toDate;
    private String pageSize;

    public Query() {
    }

    public Query(String query, String sortBy, String language, String fromDate, String toDate, String pageSize) {
        this.query = query;
        this.sortBy = sortBy;
        this.language = language;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.pageSize = pageSize;
    }

    public String getQuery() {
        return query;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getLanguage() {
        return language;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getPageSize() {
        return pageSize;
    }

    @Override
    public String toString() {
        return query + sortBy + language + fromDate + toDate + pageSize;
    }
}
