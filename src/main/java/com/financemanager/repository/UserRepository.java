package com.financemanager.repository;

import java.util.Optional;
import java.util.Set;

import com.financemanager.entity.Category;
import com.financemanager.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findByEmail(String email);
	
	Set<Category> findCategoriesById(Integer id);
}
