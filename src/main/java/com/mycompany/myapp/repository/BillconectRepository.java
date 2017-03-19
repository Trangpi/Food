package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Billconect;

import com.mycompany.myapp.domain.MonAn;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Billconect entity.

@SuppressWarnings("unused")*/
@SuppressWarnings("unused")
public interface BillconectRepository extends JpaRepository<Billconect,Long> {
 @Query(value ="select billconect.monAn from Billconect billconect group by billconect.monAn order by sum(billconect.soluong) desc")
    List<MonAn> findMonAndathang();
}
