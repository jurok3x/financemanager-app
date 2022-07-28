package com.financemanager.service;

import java.util.List;

import com.financemanager.dto.ExpenseDTO;
import com.financemanager.entity.utils.DatePart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public interface ExpenseService {
	ExpenseDTO save(ExpenseDTO exspenseDTO);
	
	ExpenseDTO update(ExpenseDTO exspenseDTO, Long id);
	
	ExpenseDTO findById(Long id);

    void delete(Long id);

    List<ExpenseDTO> findByUserIdAndCategoryIdAndDatePart(Integer userId, Integer categoryId, DatePart datePart);
    
    Page<ExpenseDTO> findByUserIdAndCategoryIdAndDatePart(Integer userId, Integer categoryId, DatePart datePart, Pageable pageable);
    
}
