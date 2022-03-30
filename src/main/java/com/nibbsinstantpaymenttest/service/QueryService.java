package com.nibbsinstantpaymenttest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nibbsinstantpaymenttest.data.dto.TransactionSearchDTO;
import com.nibbsinstantpaymenttest.model.Transaction;

public interface QueryService {
	Page<Transaction> searchTransactions(TransactionSearchDTO searchDTO, Pageable pageable);
}
