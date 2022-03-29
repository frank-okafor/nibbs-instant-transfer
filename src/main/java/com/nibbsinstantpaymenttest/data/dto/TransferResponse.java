package com.nibbsinstantpaymenttest.data.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferResponse {
	private String transactionReference;
	private String destinationAccount;
	private BigDecimal amount;
	private BigDecimal transactionFee;
	private BigDecimal totalAmount;
	private String transactionDate;
	private String transactionStatus;
}
