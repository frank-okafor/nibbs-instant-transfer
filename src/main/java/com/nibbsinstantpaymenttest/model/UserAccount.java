package com.nibbsinstantpaymenttest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_account")
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount extends BaseModel<UserAccount> implements Serializable {
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "user_id", nullable = false, unique = true)
	private String userId;
	@Column(name = "account_number", nullable = false)
	private String accountNumber;
	@Column(name = "account_balance", nullable = false)
	private BigDecimal accountBalance;
	@Column(name = "balance_as_at_date")
	private LocalDateTime balanceAsAtDate;
}
