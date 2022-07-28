package com.financemanager.repository;

import java.util.List;

import com.financemanager.entity.Expense;
import com.financemanager.entity.utils.DatePart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepository extends JpaRepository<Expense, Long> {
	
	@Query(value = "SELECT expense FROM Expense expense\n"
            + "WHERE expense.user.id = :userId AND (expense.category.id = :categoryId OR :categoryId IS NULL)\n"
            + "AND (EXTRACT(month from expense.date) = :#{#datePart.month} OR :#{#datePart.month} IS NULL)\n"
            + "AND (EXTRACT(year from expense.date) = :#{#datePart.year} OR :#{#datePart.year} IS NULL)\n"
            + "ORDER BY expense.date\n")
	List<Expense> findByUserIdAndCategoryIdAndDatePart(
			@Param("userId") Integer userId,
			@Nullable @Param("categoryId") Integer categoryId,
			@Param("datePart") DatePart datePart);
	
	   @Query(value = "SELECT expense FROM Expense expense\n"
	            + "WHERE expense.user.id = :userId AND (expense.category.id = :categoryId OR :categoryId IS NULL)\n"
	            + "AND (EXTRACT(month from expense.date) = :#{#datePart.month} OR :#{#datePart.month} IS NULL)\n"
	            + "AND (EXTRACT(year from expense.date) = :#{#datePart.year} OR :#{#datePart.year} IS NULL)\n"
	            + "ORDER BY expense.date\n")
	    Page<Expense> findByUserIdAndCategoryIdAndDatePart(
	            @Param("userId") Integer userId,
	            @Nullable @Param("categoryId") Integer categoryId,
	            @Param("datePart") DatePart datePart,
	            Pageable pageable);
}