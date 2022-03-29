package com.nibbsinstantpaymenttest.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.nibbsinstantpaymenttest.enums.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "transaction")
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends BaseModel<Transaction> implements Serializable {
	@Column(name = "transaction_reference", nullable = false)
	private String transactionReference;
	@Column(name = "amount")
	private BigDecimal amount;
	@Column(name = "transaction_fee")
	private BigDecimal transactionFee;
	@Column(name = "billed_amount")
	private BigDecimal billedAmount;
	@Column(name = "description")
	private String description;
	@Column(name = "transaction_date")
	private LocalDateTime transactionDate;
	@Column(name = "status")
	@Enumerated(value = EnumType.STRING)
	private TransactionStatus status;
	@Builder.Default
	@Column(name = "commission_worthy")
	private Boolean commissionWorthy = false;
	@Column(name = "commission")
	private BigDecimal commission;
	@Column(name = "user_id", nullable = false)
	private String userId;
	@Column(name = "destination_account", nullable = false)
	private String destinationAccount;
	@Builder.Default
	@Column(name = "processed")
	private boolean processed = false;
}
