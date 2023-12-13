package com.sberStart.bank_api.repository;

import com.sberStart.bank_api.entity.OperationBankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OperationBankRepository extends JpaRepository<OperationBankEntity, UUID> {
}
