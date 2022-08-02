package com.financemanager.repository;

import java.util.List;
import java.util.Optional;

import com.financemanager.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findByEmail(String email);
	
	@Query(value = "SELECT c.users FROM Category c WHERE c.id = :categoryId")
	List<User> findByCategoryId(@Param("categoryId") Integer categoryId);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO users_categories(user_id, category_id) VALUES(:userId, :categoryId)", nativeQuery = true)
    void addCategory(@Param("userId") Integer userId, @Param("categoryId") Integer categoryId);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM users_categories WHERE user_id = :userId AND category_id = :categoryId", nativeQuery = true)
    void removeCategory(@Param("userId") Integer userId, @Param("categoryId") Integer categoryId);
}
