package com.financemanager.repository;

import com.financemanager.entity.Income;
import com.financemanager.entity.utils.DatePart;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    
    @Query(value = "SELECT income FROM Income income\n"
            + "WHERE income.user.id = :userId\n"
            + "AND (EXTRACT(month from income.date) = :#{#datePart.month} OR :#{#datePart.month} IS NULL)\n"
            + "AND (EXTRACT(year from income.date) = :#{#datePart.year} OR :#{#datePart.year} IS NULL)\n"
            + "ORDER BY income.date\n")
    List<Income> findByUserId(
            @Param("userId")Integer userId,
            @Param("datePart") DatePart datePart);
    
    @Query(value = "SELECT income FROM Income income\n"
            + "WHERE income.user.id = :userId\n"
            + "AND (EXTRACT(month from income.date) = :#{#datePart.month} OR :#{#datePart.month} IS NULL)\n"
            + "AND (EXTRACT(year from income.date) = :#{#datePart.year} OR :#{#datePart.year} IS NULL)\n"
            + "ORDER BY income.date\n")
    List<Income> findByUserId(
            @Param("userId")Integer userId,
            @Param("datePart") DatePart datePart,
            Pageable pageable);

}
