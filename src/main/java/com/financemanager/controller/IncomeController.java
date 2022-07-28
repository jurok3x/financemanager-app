package com.financemanager.controller;

import com.financemanager.dto.IncomeDTO;
import com.financemanager.entity.utils.DatePart;
import com.financemanager.model.IncomeModel;
import com.financemanager.service.IncomeService;
import com.financemanager.service.assembler.IncomeModelAssembler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/incomes")
@AllArgsConstructor
@Slf4j
public class IncomeController {
    
    private static final String UPDATE_INCOME_INFO = "Update income with id %d";
    private static final String SAVE_INCOME_INFO = "Save income: %s";
    private static final String DELETE_INCOME_INFO = "Delete income with id %d";
    private static final String FIND_BY_USER_ID_INFO = "Handling find incomes with user_id %d and month %d and year %d";
    private static final String FIND_INCOME_ID_INFO = "Handling find income with id %d";
    private static final String INCORECT_MONTH_ERROR = "Incorect month";
    private static final String INCORRECT_PAGE_ERROR = "Incorrect page number";
    private static final String INCORRECT_SIZE_ERROR = "Incorrect page size";
    private static final String INCORRECT_YEAR_ERROR = "Incorrect year";
    private static final String INCORRECT_ID_ERROR = "Id must be greater than or equal to 1";
    
    private IncomeService incomeService;
    private IncomeModelAssembler incomeAssembler;
    
    @GetMapping("/{id}")
    public ResponseEntity<IncomeModel> findById(@PathVariable @Min(value = 1, message = INCORRECT_ID_ERROR) Long id) {
        log.info(FIND_INCOME_ID_INFO, id);
        return ResponseEntity.ok(incomeAssembler.toModel(incomeService.findById(id)));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<CollectionModel<IncomeModel>> findByUserIdAndCategoryIdAndDatePart(
            @PathVariable @Min(value = 1, message = INCORRECT_ID_ERROR) Integer userId,
            @RequestParam(required = false) @Min(value = 0, message = INCORRECT_YEAR_ERROR) Integer year,
            @RequestParam(required = false) @Min(value = 0, message = INCORECT_MONTH_ERROR) @Max(value = 12, message = INCORECT_MONTH_ERROR) Integer month) {
        log.info(FIND_BY_USER_ID_INFO, userId, month, year);
        List<IncomeDTO> incomes = incomeService.findByUserIdAndDatePart(userId, new DatePart(year, month));
        return ResponseEntity.ok(incomeAssembler.toCollectionModel(incomes));
    }
    
    @GetMapping("/page/user/{userId}")
    public ResponseEntity<Page<IncomeModel>> findByUserIdAndCategoryIdAndDatePart(
            @PathVariable @Min(value = 1, message = INCORRECT_ID_ERROR) Integer userId,
            @RequestParam(required = false) @Min(value = 0, message = INCORRECT_YEAR_ERROR) Integer year,
            @RequestParam(required = false) @Min(value = 0, message = INCORECT_MONTH_ERROR) @Max(value = 12, message = INCORECT_MONTH_ERROR) Integer month,
            @RequestParam(required = false) @Min(value = 0, message = INCORRECT_SIZE_ERROR) Integer size,
            @RequestParam(required = false) @Min(value = 0, message = INCORRECT_PAGE_ERROR) Integer page) {
        log.info(FIND_BY_USER_ID_INFO, userId, month, year);
        Page<IncomeDTO> incomesPage = incomeService.findByUserIdAndDatePart(userId, new DatePart(year, month), PageRequest.of(page, size));
        return ResponseEntity.ok(incomesPage.map(incomeAssembler::toModel));
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody IncomeDTO incomeDTO) {
        log.info(SAVE_INCOME_INFO, incomeDTO.toString());
        IncomeDTO addedIncome = incomeService.save(incomeDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedIncome.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<IncomeModel> update(
            @PathVariable @Min(value = 1, message = INCORRECT_ID_ERROR) Long id,
            @Valid @RequestBody IncomeDTO incomeDTO) {
        log.info(UPDATE_INCOME_INFO, id);
        return ResponseEntity.ok(incomeAssembler.toModel(incomeService.update(incomeDTO, id)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @Min(value = 1, message = INCORRECT_ID_ERROR) Long id) {
        log.info(DELETE_INCOME_INFO, id);
        return ResponseEntity.noContent().build();
    }

}
