package com.currency.currency_converter.dto;

import java.util.Map;

public class ExchangeRatesDTO {

    private boolean success;
    private long timestamp;
    private boolean historical;
    private String base;
    private String date;
    private Map<String, Double> rates;

    // Default constructor
    public ExchangeRatesDTO() {}

    // Parameterized constructor
    public ExchangeRatesDTO(boolean success, long timestamp, boolean historical, String base, String date, Map<String, Double> rates) {
        this.success = success;
        this.timestamp = timestamp;
        this.historical = historical;
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isHistorical() {
        return historical;
    }

    public void setHistorical(boolean historical) {
        this.historical = historical;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "ExchangeRatesDTO{" +
                "success=" + success +
                ", timestamp=" + timestamp +
                ", historical=" + historical +
                ", base='" + base + '\'' +
                ", date='" + date + '\'' +
                ", rates=" + rates +
                '}';
    }
}