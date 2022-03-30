package com.nibbsinstantpaymenttest.repository.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.nibbsinstantpaymenttest.data.dto.TransactionSearchDTO;
import com.nibbsinstantpaymenttest.model.Transaction;
import com.nibbsinstantpaymenttest.repository.TransactionRepository;
import com.nibbsinstantpaymenttest.service.QueryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QueryBuilder implements QueryService {

	private final TransactionRepository transactionRepository;

	@Override
	public Page<Transaction> searchTransactions(TransactionSearchDTO searchDTO, Pageable pageable) {
		Specification<Transaction> specification = (root, query, criteriaBuilder) -> buildSearchQuery(searchDTO, root,
				query, criteriaBuilder);
		return transactionRepository.findAll(specification, pageable);
	}

	private Predicate buildSearchQuery(TransactionSearchDTO searchDTO, Root<Transaction> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		Predicate whereClause = null;
		if (searchDTO.getToDate() != null && searchDTO.getFromDate() != null) {
			whereClause = cb.and(whereClause,
					cb.between(root.get("transactionDate"), searchDTO.getFromDate(), searchDTO.getToDate()));
		}
		if (searchDTO.getStatus() != null) {
			whereClause = cb.and(whereClause, cb.equal(root.get("status"), searchDTO.getStatus()));
		}
		if (searchDTO.getUserId() != null && !searchDTO.getUserId().isEmpty()) {
			whereClause = cb.and(whereClause, cb.equal(root.get("userId"), searchDTO.getUserId()));
		}
		return whereClause;
	}

}
