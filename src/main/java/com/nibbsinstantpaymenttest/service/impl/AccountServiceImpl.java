package com.nibbsinstantpaymenttest.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nibbsinstantpaymenttest.data.CreateUserRequest;
import com.nibbsinstantpaymenttest.data.ServiceResponse;
import com.nibbsinstantpaymenttest.data.dto.TransactionSearchDTO;
import com.nibbsinstantpaymenttest.data.dto.TransferRequestDTO;
import com.nibbsinstantpaymenttest.data.dto.TransferResponse;
import com.nibbsinstantpaymenttest.enums.TransactionStatus;
import com.nibbsinstantpaymenttest.enums.TransactionType;
import com.nibbsinstantpaymenttest.exception.ServiceCustomException;
import com.nibbsinstantpaymenttest.model.Transaction;
import com.nibbsinstantpaymenttest.model.UserAccount;
import com.nibbsinstantpaymenttest.repository.TransactionRepository;
import com.nibbsinstantpaymenttest.repository.UserAccountRepository;
import com.nibbsinstantpaymenttest.service.AccountService;
import com.nibbsinstantpaymenttest.service.QueryService;
import com.nibbsinstantpaymenttest.utils.AppUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	private final TransactionRepository transactionRepository;
	private final UserAccountRepository userAccountRepository;
	private final QueryService queryService;

	@Override
	public ServiceResponse<UserAccount> createNewUser(CreateUserRequest request) {
		String userId = "app_user-" + UUID.randomUUID().toString();
		String accountNumber = AppUtils.generateAccountNumber();
		BigDecimal balance = request.getStartingBalance() == null ? BigDecimal.ZERO : request.getStartingBalance();
		UserAccount user = UserAccount.builder().accountBalance(balance).accountNumber(accountNumber).userId(userId)
				.build();
		return new ServiceResponse<UserAccount>(HttpStatus.OK, "user created successfully",
				userAccountRepository.save(user));
	}

	@Override
	public ServiceResponse<TransferResponse> initiateTransfer(TransferRequestDTO request) {
		Optional<UserAccount> userWrap = userAccountRepository.findByUserId(request.getUserId());
		if (!userWrap.isPresent()) {
			throw new ServiceCustomException(HttpStatus.BAD_REQUEST, "invalid user is");
		}
		if (request.getAmount().compareTo(new BigDecimal(100)) < 0) {
			throw new ServiceCustomException(HttpStatus.BAD_REQUEST, "invalid amount: must be 100 and above");
		}
		BigDecimal fee = AppUtils.getTransactionFee(request.getAmount());
		BigDecimal totalBilled = request.getAmount().add(fee);
		if (userWrap.get().getAccountBalance().compareTo(totalBilled) < 0) {
			throw new ServiceCustomException(HttpStatus.BAD_REQUEST, "insufficient balance");
		}
		String reference = "Ref-" + UUID.randomUUID().toString();
		Transaction transaction = Transaction.builder().amount(request.getAmount()).billedAmount(totalBilled)
				.commissionWorthy(false).description(reference).status(TransactionStatus.PENDING)
				.transactionReference(reference).transactionDate(LocalDateTime.now()).transactionFee(fee)
				.destinationAccount(request.getDestinationAccount()).userId(userWrap.get().getUserId()).processed(false)
				.build();
		transactionRepository.save(transaction);
		updateUserBalance(userWrap.get(), totalBilled, TransactionType.DEBIT);
		TransferResponse response = TransferResponse.builder().amount(request.getAmount())
				.destinationAccount(request.getDestinationAccount()).totalAmount(totalBilled)
				.transactionDate(transaction.getTransactionDate().toString()).transactionFee(fee)
				.transactionStatus(TransactionStatus.PENDING.name()).build();
		return new ServiceResponse<TransferResponse>(HttpStatus.OK, "transaction in processing", response);
	}

	@Override
	public ServiceResponse<Page<Transaction>> getTransactions(LocalDate fromDate, LocalDate toDate, String userId,
			String status, int pageNumber, int pageSize) {
		TransactionSearchDTO searchDTO = TransactionSearchDTO.builder().fromDate(fromDate.atStartOfDay())
				.toDate(toDate.atTime(23, 59, 59)).userId(userId).status(TransactionStatus.valueOf(status)).build();
		Page<Transaction> result = queryService.searchTransactions(searchDTO, AppUtils.getPage(pageNumber, pageSize));
		return new ServiceResponse<Page<Transaction>>(HttpStatus.OK, "records retrieved successfully", result);
	}

	@Override
	public synchronized void updateUserBalance(UserAccount user, BigDecimal amount, TransactionType type) {
		BigDecimal balance = BigDecimal.ZERO;
		switch (type.name()) {
		case "CREDIT":
			balance = user.getAccountBalance().add(amount);
			user.setAccountBalance(balance);
			break;
		case "DEBIT":
			balance = user.getAccountBalance().subtract(amount);
			user.setAccountBalance(balance);
			break;
		default:
			break;
		}
		user.setBalanceAsAtDate(LocalDateTime.now());
		userAccountRepository.save(user);
	}
}
