package com.nibbsinstantpaymenttest.cronjobs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.nibbsinstantpaymenttest.enums.TransactionStatus;
import com.nibbsinstantpaymenttest.model.NibbsRecord;
import com.nibbsinstantpaymenttest.model.Transaction;
import com.nibbsinstantpaymenttest.model.UserAccount;
import com.nibbsinstantpaymenttest.repository.NibbsRecordRepository;
import com.nibbsinstantpaymenttest.repository.TransactionRepository;
import com.nibbsinstantpaymenttest.repository.UserAccountRepository;
import com.nibbsinstantpaymenttest.utils.AppUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CronJobs {
	private final TransactionRepository transactionRepository;
	private final NibbsRecordRepository nibbsRecordRepository;
	private final UserAccountRepository userAccountRepository;

	void processTransactions() {
		List<Transaction> toProcess = transactionRepository.findByProcessed(false);
		if (toProcess.isEmpty())
			return;
		toProcess.stream().forEach(transaction -> {
			UserAccount user = userAccountRepository.findByUserId(transaction.getUserId()).get();
			transaction.setProcessed(true);
			transaction.setCommissionWorthy(true);
			transaction.setStatus(TransactionStatus.SUCCESSFUL);
			transactionRepository.save(transaction);
			NibbsRecord record = NibbsRecord.builder().amount(transaction.getAmount())
					.recieverAccountNumber(transaction.getDestinationAccount())
					.senderAccountNumber(user.getAccountNumber()).transactionCharge(transaction.getTransactionFee())
					.transactionDate(LocalDateTime.now()).transactionReference(transaction.getTransactionReference())
					.transactionStatus(TransactionStatus.SUCCESSFUL).build();
			nibbsRecordRepository.save(record);

		});
	}

	void processCommission() {
		List<Transaction> toProcess = transactionRepository.findByCommissionWorthy(true);
		if (toProcess.isEmpty())
			return;
		toProcess.stream().forEach(transaction -> {
			BigDecimal commisionAmount = AppUtils.getCommission(transaction.getTransactionFee());
			transaction.setCommission(commisionAmount);
			transactionRepository.save(transaction);
		});
	}
}
