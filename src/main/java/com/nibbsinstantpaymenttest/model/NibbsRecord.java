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
@Table(name = "nibbs_record")
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NibbsRecord extends BaseModel<NibbsRecord> implements Serializable {
	@Column(name = "sender_account_number")
	private String senderAccountNumber;
	@Column(name = "reciever_account_number")
	private String recieverAccountNumber;
	@Column(name = "transaction_reference")
	private String transactionReference;
	@Column(name = "amount")
	private BigDecimal amount;
	@Column(name = "transaction_charge")
	private BigDecimal transactionCharge;
	@Column(name = "status")
	@Enumerated(value = EnumType.STRING)
	private TransactionStatus transactionStatus;
	@Column(name = "transaction_date")
	private LocalDateTime transactionDate;
}
