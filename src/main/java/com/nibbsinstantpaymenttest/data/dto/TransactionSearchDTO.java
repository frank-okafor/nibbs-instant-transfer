package com.nibbsinstantpaymenttest.data.dto;

import java.time.LocalDateTime;

import com.nibbsinstantpaymenttest.enums.TransactionStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionSearchDTO {
	@Builder.Default
	private String userId = "";
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private TransactionStatus status;
}
