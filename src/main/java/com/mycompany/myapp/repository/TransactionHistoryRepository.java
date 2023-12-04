package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TransactionHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransactionHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {}
