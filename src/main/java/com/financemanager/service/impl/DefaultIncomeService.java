package com.financemanager.service.impl;

import com.financemanager.dto.IncomeDTO;
import com.financemanager.entity.utils.DatePart;
import com.financemanager.exception.ResourceNotFoundException;
import com.financemanager.mapper.IncomeMapper;
import com.financemanager.repository.IncomeRepository;
import com.financemanager.service.IncomeService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@PropertySource(value = { "classpath:/messages/income/info.properties" })
public class DefaultIncomeService implements IncomeService {
    
    private final IncomeRepository incomeRepository;
    private final IncomeMapper incomeMapper;
    
    @Value("${not_found.error}")
    private String incomeNotFoundError;

    @Override
    public IncomeDTO save(IncomeDTO incomeDTO) {
        return incomeMapper.toIncomeDTO(incomeRepository.save(incomeMapper.toIncome(incomeDTO)));
    }

    @Override
    public List<IncomeDTO> findByUserIdAndDatePart(Integer userId, DatePart datePart) {
        return incomeRepository.findByUserIdAndDatePart(userId, datePart).stream().map(incomeMapper::toIncomeDTO).collect(Collectors.toList());
    }

    @Override
    public Page<IncomeDTO> findByUserIdAndDatePart(Integer userId, DatePart datePart, Pageable pageable) {
        return incomeRepository.findByUserIdAndDatePart(userId, datePart, pageable).map(incomeMapper::toIncomeDTO);
    }

    @Override
    public IncomeDTO update(IncomeDTO incomeDTO, Long id) {
        incomeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(incomeNotFoundError, id)));
        return incomeMapper.toIncomeDTO(incomeRepository.save(incomeMapper.toIncome(incomeDTO)));
    }

    @Override
    public void delete(Long id) {
        IncomeDTO incomeDTO = incomeRepository.findById(id).map(incomeMapper::toIncomeDTO).orElseThrow(
                () -> new ResourceNotFoundException(String.format(incomeNotFoundError, id)));
        incomeRepository.deleteById(incomeDTO.getId());
    }

    @Override
    public IncomeDTO findById(Long id) {
        IncomeDTO income = incomeRepository.findById(id).map(incomeMapper::toIncomeDTO).orElseThrow(
                () -> new ResourceNotFoundException(String.format(incomeNotFoundError, id)));
        return income;
    }

}
