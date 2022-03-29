package com.nibbsinstantpaymenttest.utils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class AppUtils {
	private static final Random RANDOM = new SecureRandom();
	private static final String ALPHABET = "1234567890987654321";

	public static String generateAccountNumber() {
		int limit = RANDOM.nextInt((10 - 1) + 1) + 1;
		StringBuilder main = new StringBuilder(limit);
		for (int i = 0; i < limit; i++) {
			main.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return main.toString();
	}

	public static BigDecimal getTransactionFee(BigDecimal originalAmount) {
		BigDecimal fee = originalAmount.divide(new BigDecimal(100)).multiply(new BigDecimal(0.5));
		return fee;
	}

	public static BigDecimal getCommission(BigDecimal originalAmount) {
		BigDecimal commission = originalAmount.divide(new BigDecimal(100)).multiply(new BigDecimal(20));
		return commission;
	}

	public static Pageable getPage(int pageNumber, int pageSize) {
		Sort sort = Sort.by(Sort.Order.desc("dateCreated").ignoreCase());
		pageNumber = ((pageNumber <= 0 ? 0 : pageNumber - 1) * pageSize);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		return pageable;
	}

}
