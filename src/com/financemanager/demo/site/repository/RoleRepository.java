package com.financemanager.demo.site.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financemanager.demo.site.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByName(String name);
}
