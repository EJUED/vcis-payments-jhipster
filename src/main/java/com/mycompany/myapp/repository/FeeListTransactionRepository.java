package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FeeListTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FeeListTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeeListTransactionRepository extends JpaRepository<FeeListTransaction, Long> {}
