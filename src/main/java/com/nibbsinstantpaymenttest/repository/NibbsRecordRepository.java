package com.nibbsinstantpaymenttest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nibbsinstantpaymenttest.model.NibbsRecord;

@Repository
public interface NibbsRecordRepository extends JpaRepository<NibbsRecord, Long> {

}
