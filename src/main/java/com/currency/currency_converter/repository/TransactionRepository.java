package com.currency.currency_converter.repository;

import com.currency.currency_converter.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
