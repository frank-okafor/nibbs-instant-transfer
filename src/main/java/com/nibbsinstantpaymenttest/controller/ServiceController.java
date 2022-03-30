package com.nibbsinstantpaymenttest.controller;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nibbsinstantpaymenttest.data.CreateUserRequest;
import com.nibbsinstantpaymenttest.data.ServiceResponse;
import com.nibbsinstantpaymenttest.data.dto.TransferRequestDTO;
import com.nibbsinstantpaymenttest.data.dto.TransferResponse;
import com.nibbsinstantpaymenttest.model.Transaction;
import com.nibbsinstantpaymenttest.model.UserAccount;
import com.nibbsinstantpaymenttest.service.AccountService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment/")
@RequiredArgsConstructor
public class ServiceController {
	private final AccountService accountService;

	@PostMapping("create-user")
	@ApiOperation(value = "create a new user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successful", response = ServiceResponse.class),
			@ApiResponse(code = 500, message = "internal error - critical!", response = ServiceResponse.class) })
	public ResponseEntity<ServiceResponse<UserAccount>> createNewUser(@Valid @RequestBody CreateUserRequest request) {
		ServiceResponse<UserAccount> response = accountService.createNewUser(request);
		return new ResponseEntity<>(response, response.getStatus());
	}

	@PostMapping("initiate-transfer")
	@ApiOperation(value = "make transfer request")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successful", response = ServiceResponse.class),
			@ApiResponse(code = 500, message = "internal error - critical!", response = ServiceResponse.class) })
	public ResponseEntity<ServiceResponse<TransferResponse>> initiateTransfer(
			@Valid @RequestBody TransferRequestDTO request) {
		ServiceResponse<TransferResponse> response = accountService.initiateTransfer(request);
		return new ResponseEntity<>(response, response.getStatus());
	}

	@GetMapping("get-transactions")
	@ApiOperation(value = "get list of transactions : paginated")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successful", response = ServiceResponse.class),
			@ApiResponse(code = 500, message = "internal error - critical!", response = ServiceException.class) })
	public ResponseEntity<ServiceResponse<Page<Transaction>>> getAllBooksInLibrary(
			@RequestParam(value = "pageNumber", required = true, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = true, defaultValue = "10") int pageSize,
			@ApiParam(value = "Format: dd/MM/yyyy") @DateTimeFormat(pattern = "dd/MM/yyyy") @RequestParam(value = "fromDate", required = false) LocalDate fromDate,
			@ApiParam(value = "Format: dd/MM/yyyy") @DateTimeFormat(pattern = "dd/MM/yyyy") @RequestParam(value = "toDate", required = false) LocalDate toDate,
			@ApiParam(value = "user Id") @RequestParam(value = "userId", required = false) String userId,
			@ApiParam(value = "Transaction status: SUCCESSFUL, FAILED, PENDING, INSUFFICIENT_FUND") @Pattern(regexp = "^SUCCESSFUL|FAILED|PENDING|INSUFFICIENT_FUND$") @RequestParam(value = "status", required = false, defaultValue = "SUCCESSFUL") String status) {
		ServiceResponse<Page<Transaction>> response = accountService.getTransactions(fromDate, toDate, userId, status,
				pageNumber, pageSize);
		return new ResponseEntity<>(response, response.getStatus());
	}

}
