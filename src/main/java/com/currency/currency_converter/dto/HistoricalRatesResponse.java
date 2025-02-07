package com.currency.currency_converter.dto;

import java.util.Map;

public class HistoricalRatesResponse {
    private String date;
    private Map<String, Double> conversionRates;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalRates;

    public HistoricalRatesResponse(String date, Map<String, Double> conversionRates, int pageNumber, int pageSize, int totalPages, long totalRates) {
        this.date = date;
        this.conversionRates = conversionRates;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalRates = totalRates;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Double> getConversionRates() {
        return conversionRates;
    }

    public void setConversionRates(Map<String, Double> conversionRates) {
        this.conversionRates = conversionRates;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalRates() {
        return totalRates;
    }

    public void setTotalRates(long totalRates) {
        this.totalRates = totalRates;
    }

}