package com.nibbsinstantpaymenttest.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.nibbsinstantpaymenttest.data.CreateUserRequest;
import com.nibbsinstantpaymenttest.data.ServiceResponse;
import com.nibbsinstantpaymenttest.data.dto.TransferRequestDTO;
import com.nibbsinstantpaymenttest.enums.TransactionStatus;
import com.nibbsinstantpaymenttest.exception.ServiceCustomException;
import com.nibbsinstantpaymenttest.model.Transaction;
import com.nibbsinstantpaymenttest.model.UserAccount;
import com.nibbsinstantpaymenttest.repository.TransactionRepository;
import com.nibbsinstantpaymenttest.repository.UserAccountRepository;
import com.nibbsinstantpaymenttest.service.impl.AccountServiceImpl;
import com.nibbsinstantpaymenttest.utils.AppUtils;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountServiceTest {

	@Mock
	TransactionRepository transactionRepository;
	@Mock
	UserAccountRepository userAccountRepository;
	@InjectMocks
	AccountServiceImpl accountServiceImpl;

	private CreateUserRequest createUserRequest;
	private BigDecimal balance;

	@BeforeEach
	void setUp() throws Exception {
		balance = new BigDecimal("4000.00");
		createUserRequest = CreateUserRequest.builder().StartingBalance(balance).name("foo").build();
	}

	@Test
	public void testCreateNewUser() {
		String userId = "app_user-" + UUID.randomUUID().toString();
		String accountNumber = AppUtils.generateAccountNumber();
		UserAccount userAccount = UserAccount.builder().accountBalance(balance).accountNumber(accountNumber)
				.userId(userId).build();
		when(userAccountRepository.save(any(UserAccount.class))).thenReturn(userAccount);
		ServiceResponse<UserAccount> newUserAccount = accountServiceImpl.createNewUser(createUserRequest);
		assertNotNull(newUserAccount);
		assertEquals(newUserAccount.getStatus(), HttpStatus.OK);
		assertEquals(newUserAccount.getMessage(), "user created successfully");
		verify(userAccountRepository, times(1)).save(any(UserAccount.class));

	}

	@Test
	public void test_InitiateTransfer() {
		TransferRequestDTO request = new TransferRequestDTO(new BigDecimal("200.00"), "2", "0908975878");

		BigDecimal fee = AppUtils.getTransactionFee(request.getAmount());

		BigDecimal totalBilled = request.getAmount().add(fee);

		String reference = "Ref-" + UUID.randomUUID().toString();

		UserAccount userAccount = UserAccount.builder().userId(request.getUserId()).name("boo")
				.accountNumber(request.getDestinationAccount()).accountBalance(new BigDecimal("400.00")).build();

		Transaction transaction = Transaction.builder().amount(request.getAmount()).billedAmount(totalBilled)
				.commissionWorthy(false).description(reference).status(TransactionStatus.PENDING)
				.transactionReference(reference).transactionDate(LocalDateTime.now()).transactionFee(fee)
				.destinationAccount(request.getDestinationAccount()).userId(userAccount.getUserId()).processed(false)
				.build();

		lenient().when(userAccountRepository.findByUserId(anyString())).thenReturn(Optional.of(userAccount));
		lenient().when(transactionRepository.save(transaction)).thenReturn(transaction);
		ServiceResponse<?> transfer = accountServiceImpl.initiateTransfer(request);

		verify(userAccountRepository, times(1)).findByUserId(anyString());
		verify(transactionRepository, times(1)).save(any());
		assertEquals(transfer.getStatus(), HttpStatus.OK);
		assertEquals(transfer.getMessage(), "transaction in processing");

		when(userAccountRepository.findByUserId(anyString())).thenReturn(Optional.empty());
		assertThatThrownBy(() -> accountServiceImpl.initiateTransfer(request))
				.isInstanceOf(ServiceCustomException.class).hasMessageContaining("invalid user is");

	}
}
