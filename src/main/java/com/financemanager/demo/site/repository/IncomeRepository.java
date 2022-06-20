package com.financemanager.demo.site.repository;

import com.financemanager.demo.site.entity.Income;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    
    List<Income> findByUserId(Integer userId);

}
