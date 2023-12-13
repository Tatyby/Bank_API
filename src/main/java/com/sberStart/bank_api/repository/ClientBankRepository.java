package com.sberStart.bank_api.repository;

import com.sberStart.bank_api.entity.ClientBankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientBankRepository extends JpaRepository<ClientBankEntity, UUID> {

}
