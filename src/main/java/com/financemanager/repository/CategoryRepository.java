package com.financemanager.repository;

import com.financemanager.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    @Query(value = "SELECT u.categories FROM User u WHERE u.id = :user_id")
    List<Category> findByUserId(@Param("user_id") Integer userId);
	
}
