package com.financemanager.service;

import com.financemanager.dto.IncomeDTO;
import com.financemanager.entity.utils.DatePart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IncomeService {
    
    IncomeDTO save(IncomeDTO incomeDTO);
    
    List<IncomeDTO> findByUserIdAndDatePart(Integer userId, DatePart datePart);
    
    Page<IncomeDTO> findByUserIdAndDatePart(Integer userId, DatePart datePart, Pageable pageable);
    
    IncomeDTO update(IncomeDTO incomeDTO, Long id);
    
    void delete(Long id);

}
