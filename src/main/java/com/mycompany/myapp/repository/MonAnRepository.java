package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MonAn;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MonAn entity.
 */
@SuppressWarnings("unused")
public interface MonAnRepository extends JpaRepository<MonAn,Long> {
 @Query ("select monan from MonAn monan where monan.theloai =?1")
  public List<MonAn> findMonAntheloai(String theloai);

}
