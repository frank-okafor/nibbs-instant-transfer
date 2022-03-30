package com.nibbsinstantpaymenttest.data.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferRequestDTO {
	@NotNull(message = "amount cannot be null")
	private BigDecimal amount;
	@NotNull(message = "user Id cannot be null")
	@NotEmpty(message = "user Id cannot be empty")
	private String userId;
	@NotNull(message = "destination Account cannot be null")
	@NotEmpty(message = "destination Account cannot be empty")
	@Pattern(regexp = "^[0-9]{10}$", message = "destination Account must be 10 only digits")
	private String destinationAccount;
}
