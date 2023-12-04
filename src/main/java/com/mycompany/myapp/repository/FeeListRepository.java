package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FeeList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FeeList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeeListRepository extends JpaRepository<FeeList, Long> {}
