package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Bill;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bill entity.
 */
@SuppressWarnings("unused")
public interface BillRepository extends JpaRepository<Bill,Long> {

    @Query("select bill from Bill bill where bill.user.login = ?#{principal.username}")
    List<Bill> findByUserIsCurrentUser();

}
