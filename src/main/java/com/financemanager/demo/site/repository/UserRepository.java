package com.financemanager.demo.site.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.financemanager.demo.site.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByLogin(String login);
	
	List<User> findByRoleId(Integer id);
}
