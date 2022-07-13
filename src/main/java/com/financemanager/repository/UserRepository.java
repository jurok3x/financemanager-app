package com.financemanager.repository;

import java.util.List;
import java.util.Optional;

import com.financemanager.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findByEmail(String email);
	
	@Query(value = "SELECT c.users FROM Category c WHERE c.id = :categoryId")
	List<User> findByCategoryId(@Param("categoryId") Integer categoryId);
}
