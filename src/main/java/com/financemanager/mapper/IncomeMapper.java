package com.financemanager.mapper;

import com.financemanager.dto.IncomeDTO;
import com.financemanager.entity.Income;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class IncomeMapper {
    
    public IncomeDTO toIncomeDTO (Income income) {
        IncomeDTO incomeDTO = new IncomeDTO();
        incomeDTO.setId(income.getId());
        incomeDTO.setAmount(income.getAmount());
        incomeDTO.setName(income.getName());
        incomeDTO.setDate(income.getDate());
        incomeDTO.setUserId(income.getUser().getId());
        return incomeDTO;
    }
    
    public Income toIncome (IncomeDTO incomeDTO) {
        Income income = new Income();
        income.setId(incomeDTO.getId());
        income.setAmount(incomeDTO.getAmount());
        income.setName(incomeDTO.getName());
        income.setDate(incomeDTO.getDate());
        return income;
    }

}
