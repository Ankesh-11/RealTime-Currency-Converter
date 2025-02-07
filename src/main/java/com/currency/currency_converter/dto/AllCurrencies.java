package com.currency.currency_converter.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
@Data
public class AllCurrencies {
    private HashMap<String,Double> conversion_rates;

    public HashMap<String, Double> getConversion_rates() {
        return conversion_rates;
    }

    public void setConversion_rates(HashMap<String, Double> conversion_rates) {
        this.conversion_rates = conversion_rates;
    }
}
