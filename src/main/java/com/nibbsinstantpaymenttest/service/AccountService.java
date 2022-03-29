package com.nibbsinstantpaymenttest.service;

import java.math.BigDecimal;

import com.nibbsinstantpaymenttest.data.CreateUserRequest;
import com.nibbsinstantpaymenttest.data.ServiceResponse;
import com.nibbsinstantpaymenttest.data.dto.TransferRequestDTO;
import com.nibbsinstantpaymenttest.data.dto.TransferResponse;
import com.nibbsinstantpaymenttest.enums.TransactionType;
import com.nibbsinstantpaymenttest.model.UserAccount;

public interface AccountService {

	ServiceResponse<UserAccount> createNewUser(CreateUserRequest request);

	ServiceResponse<TransferResponse> initiateTransfer(TransferRequestDTO request);

	void updateUserBalance(UserAccount user, BigDecimal amount, TransactionType type);
}
