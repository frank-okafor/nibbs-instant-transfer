package com.nibbsinstantpaymenttest.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.nibbsinstantpaymenttest.data.CreateUserRequest;
import com.nibbsinstantpaymenttest.data.ServiceResponse;
import com.nibbsinstantpaymenttest.data.dto.TransferRequestDTO;
import com.nibbsinstantpaymenttest.data.dto.TransferResponse;
import com.nibbsinstantpaymenttest.enums.TransactionType;
import com.nibbsinstantpaymenttest.model.Transaction;
import com.nibbsinstantpaymenttest.model.UserAccount;

public interface AccountService {

	ServiceResponse<UserAccount> createNewUser(CreateUserRequest request);

	ServiceResponse<TransferResponse> initiateTransfer(TransferRequestDTO request);

	void updateUserBalance(UserAccount user, BigDecimal amount, TransactionType type);

	ServiceResponse<Page<Transaction>> getTransactions(LocalDate fromDate, LocalDate toDate, String userId,
			String status, int pageNumber, int pageSize);
}
