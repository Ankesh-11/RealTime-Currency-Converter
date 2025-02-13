package com.currency.currency_converter.controller;

import com.currency.currency_converter.dto.*;
import com.currency.currency_converter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/convert")
    public String showCurrencyConverterPage(Model model) {
        AllCurrencies response = currencyService.getAvailableCurrencies();
        model.addAttribute("currencies", response.getConversion_rates().keySet());
        model.addAttribute("transaction", new TransactionRequest());
        return "currency-converter";
    }

    @PostMapping("/convert")
    public String convertCurrency(@ModelAttribute TransactionRequest request, Model model) {
        TransactionResponse convertedTransaction = currencyService.convertCurrency(request);
        model.addAttribute("convertedAmount", convertedTransaction.getConvertedAmount());
        model.addAttribute("currencies", currencyService.getAvailableCurrencies().getConversion_rates().keySet());
        model.addAttribute("transaction", request);
        return "currency-converter";
    }

    @GetMapping("/historical-rates")
    public String getHistoricalRates(
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            Model model) {
        if (date != null && !date.isEmpty()) {
            LocalDate parsedDate = LocalDate.parse(date);
            HistoricalRatesResponse historicalRates = currencyService.getHistoricalRates(parsedDate.toString(), pageNumber, pageSize);
            model.addAttribute("date", date);
            model.addAttribute("rates", historicalRates.getConversionRates()
            );
            model.addAttribute("currentPage", pageNumber);
            model.addAttribute("totalPages", historicalRates.getTotalPages());
            model.addAttribute("pageSize", pageSize);
        }
        return "historical-rates";
    }

    @GetMapping("/convertedCurrency")
    public String getConvertedCurrencies(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            Model model) {
        List<TransactionResponse> allConvertedCurrencies = currencyService.getAllConvertedCurrencies(pageNumber, pageSize);
        model.addAttribute("transactions", allConvertedCurrencies);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        return "converted-currencies";
    }
}
