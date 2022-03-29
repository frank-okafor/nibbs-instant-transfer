package com.nibbsinstantpaymenttest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nibbsinstantpaymenttest.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findByProcessed(Boolean processed);

	List<Transaction> findByCommissionWorthy(Boolean commissionWorthy);
}
