package com.currency.currency_converter.service;

import com.currency.currency_converter.dto.*;
import com.currency.currency_converter.model.Transaction;
import com.currency.currency_converter.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CurrencyService {

    @Value("${api.currency.url}")
    private String apiUrl;

    @Value("${apiKey}")
    private String apiKey;

    @Value("${api.historical.rates}")
    private String getHistoricalRatesApiUrl;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private  ModelMapper modelMapper;


    private  RestTemplate restTemplate = new RestTemplate();

    public AllCurrencies getAvailableCurrencies() {
        ApiResponse apiResponse = restTemplate.getForObject(apiUrl, ApiResponse.class);

        AllCurrencies allCurrencies = new AllCurrencies();
        if (apiResponse != null) {
            allCurrencies.setConversion_rates(apiResponse.getConversion_rates());
        }
        return allCurrencies;
    }
    public TransactionResponse convertCurrency(TransactionRequest request) {

        ApiResponse response = restTemplate.getForObject(apiUrl, ApiResponse.class);

        if (response != null && response.getResult().equals("success")) {

            double fromRate = response.getConversion_rates().get(request.getFromCurrency());
            double toRate = response.getConversion_rates().get(request.getToCurrency());
            double conversionRate = toRate / fromRate;

            double convertedAmount = BigDecimal.valueOf(request.getAmount() * conversionRate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            Transaction transaction = modelMapper.map(request, Transaction.class);
            transaction.setConvertedAmount(convertedAmount);
            transaction.setTransactionDate(LocalDateTime.now());

            Transaction savedTransaction = transactionRepository.save(transaction);
            return modelMapper.map(savedTransaction, TransactionResponse.class);

//          modelMapper.createTypeMap(TransactionRequest.class, Transaction.class)
//                    .addMappings(mapper -> {
//                        mapper.map(TransactionRequest::getAmount, Transaction::setAmount);
//                        mapper.map(TransactionRequest::getFromCurrency, Transaction::setFromCurrency);
//                        mapper.map(TransactionRequest::getToCurrency, Transaction::setToCurrency);
//                    });

        }
        throw new RuntimeException("Failed to fetch exchange rate from API.");
    }


    public HistoricalRatesResponse getHistoricalRates(String date, int pageNumber, int pageSize) {

        final String historicalApiUrl = getHistoricalRatesApiUrl + date + "?access_key=" + apiKey;

        ExchangeRatesDTO exchangeRatesDTO = restTemplate.getForObject(historicalApiUrl, ExchangeRatesDTO.class);

        if (exchangeRatesDTO != null && !exchangeRatesDTO.getRates().isEmpty()) {
            List<Map.Entry<String, Double>> ratesList = new ArrayList<>(exchangeRatesDTO.getRates().entrySet());

            int totalRates = ratesList.size();
            int totalPages = (int) Math.ceil((double) totalRates / pageSize);

            if (pageNumber < 0 || pageNumber >= totalPages) {
                throw new IllegalArgumentException("Page number out of range.");
            }
            int start = pageNumber * pageSize;
            int end = Math.min(start + pageSize, totalRates);
            List<Map.Entry<String, Double>> paginatedRates = ratesList.subList(start, end);

            Map<String, Double> paginatedRatesMap = paginatedRates.stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            return new HistoricalRatesResponse(
                    date,
                    paginatedRatesMap,
                    pageNumber,
                    pageSize,
                    totalPages,
                    totalRates
            );
        } else {
            throw new RuntimeException("Failed to fetch historical exchange rates.");
        }
    }

    public List<TransactionResponse> getAllConvertedCurrencies(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize);

        Page<Transaction> pageTransaction = transactionRepository.findAll(pageable);

        return pageTransaction.getContent().stream()
                .map(transaction -> modelMapper.map(transaction, TransactionResponse.class))
                .collect(Collectors.toList());
    }

}
