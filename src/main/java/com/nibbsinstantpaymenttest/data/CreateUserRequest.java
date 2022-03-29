package com.nibbsinstantpaymenttest.data;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {
	@NotNull(message = "name cannot be null")
	@NotEmpty(message = "name cannot be empty")
	private String name;
	private BigDecimal StartingBalance;
}
