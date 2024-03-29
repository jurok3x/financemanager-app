package com.financemanager.controller;

import com.financemanager.dto.IncomeDTO;
import com.financemanager.entity.utils.DatePart;
import com.financemanager.service.IncomeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
@Slf4j
@PropertySource(value = { "classpath:/messages/income/info.properties" })
@SecurityRequirement(name = "bearerAuth")
public class IncomeController {
    
    private final IncomeService incomeService;
    
    @Value("${update.info}")
    private String updateInfo;
    @Value("${save.info}")
    private String saveInfo;
    @Value("${delete.info}")
    private String deleteInfo;
    @Value("${find_by_user_id.info}")
    private String findByUserIdInfo;
    @Value("${find_by_id.info}")
    private String findByIdInfo;
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('income:read')")
    public ResponseEntity<IncomeDTO> findById(@PathVariable Long id) {
        log.info(findByIdInfo, id);
        return ResponseEntity.ok(incomeService.findById(id));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id && hasAuthority('income:read')") 
    public ResponseEntity<List<IncomeDTO>> findByUserIdAndCategoryIdAndDatePart(
            @PathVariable Integer userId,
            @RequestParam(required = false) @Min(value = 0) Integer year,
            @RequestParam(required = false) @Min(value = 0) @Max(value = 12) Integer month) {
        log.info(findByUserIdInfo, userId, month, year);
        return ResponseEntity.ok(incomeService.findByUserIdAndDatePart(userId, new DatePart(year, month)));
    }
    
    @GetMapping("/page/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id && hasAuthority('income:read')") 
    public ResponseEntity<Page<IncomeDTO>> findByUserIdAndCategoryIdAndDatePart(
            @PathVariable Integer userId,
            @RequestParam(required = false) @Min(value = 0) Integer year,
            @RequestParam(required = false) @Min(value = 0) @Max(value = 12) Integer month,
            @RequestParam(required = false) @Min(value = 0) Integer size,
            @RequestParam(required = false) @Min(value = 0) Integer page) {
        log.info(findByUserIdInfo, userId, month, year);
        return ResponseEntity.ok(incomeService.findByUserIdAndDatePart(userId, new DatePart(year, month), PageRequest.of(page, size)));
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('income:write')") 
    public ResponseEntity<?> save(@RequestBody IncomeDTO incomeDTO) {
        log.info(saveInfo, incomeDTO.toString());
        return new ResponseEntity<>(incomeService.save(incomeDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('income:write')")
    public ResponseEntity<IncomeDTO> update(@RequestBody IncomeDTO incomeDTO, @PathVariable Long id) {
        log.info(updateInfo, id);
        return ResponseEntity.ok(incomeService.update(incomeDTO, id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.info(deleteInfo, id);
        incomeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
