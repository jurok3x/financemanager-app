package com.financemanager.controller;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.financemanager.dto.ExpenseDTO;
import com.financemanager.entity.utils.DatePart;
import com.financemanager.exception.ResourceNotFoundException;
import com.financemanager.model.ExpenseModel;
import com.financemanager.service.ExpenseService;
import com.financemanager.service.assembler.ExpenseModelAssembler;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/api/expenses")
@AllArgsConstructor
@Log
@SecurityRequirement(name = "bearerAuth")
public class ExpensesController {
    private static final String UPDATE_EXPENSE_INFO = "Handling update expense with id %d";
    private static final String SAVE_EXPENSE_INFO = "Handling save new expense %s";
    private static final String DELETE_EXPENSE_INFO = "Handling delete expense with id %d";
	private static final String FIND_BY_CATEGORY_ID_AND_DATE_INFO = "Handling find expenses with category ID %1$d of year %2$d and month %3$d";
    private static final String FIND_BY_ID_INFO = "Handling find expense with ID %d";
    private static final String INCORECT_MONTH_ERROR = "Incorect month";
    private static final String INCORRECT_PAGE_ERROR = "Incorrect page number";
    private static final String INCORRECT_SIZE_ERROR = "Incorrect page size";
    private static final String INCORRECT_YEAR_ERROR = "Incorrect year";
    private static final String INCORRECT_ID_ERROR = "Id must be greater than or equal to 1";
    private ExpenseService expensesService;
	private ExpenseModelAssembler expensesAssembler;

	@GetMapping("/{id}")
	public ResponseEntity<ExpenseModel> findById(@PathVariable
			@Min(value = 1, message = INCORRECT_ID_ERROR) Long id) throws ResourceNotFoundException {
		log.info(String.format(FIND_BY_ID_INFO, id));
	    return ResponseEntity.ok(expensesAssembler.toModel(expensesService.findById(id)));
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<CollectionModel<ExpenseModel>> findByUserIdAndCategoryIdAndDatePart(
	        @PathVariable(name = "userId") Integer userId,
	        @RequestParam(required = false) Integer categoryId,
			@RequestParam(required = false) @Min(value = 0, message = INCORRECT_YEAR_ERROR) Integer year,
	        @RequestParam(required = false) @Min(value = 0, message = INCORECT_MONTH_ERROR) @Max(value = 12, message = INCORECT_MONTH_ERROR) Integer month) {
		log.info(String.format(FIND_BY_CATEGORY_ID_AND_DATE_INFO, categoryId, year, month));
		List<ExpenseDTO> items = expensesService.findByUserIdAndCategoryIdAndDatePart(userId, categoryId, new DatePart(year, month));
		return new ResponseEntity<>(
				expensesAssembler.toCollectionModel(items),
				HttpStatus.OK);
	}
	
	@GetMapping("/page/user/{userId}")
    public ResponseEntity<Page<ExpenseModel>> findByUserIdAndCategoryIdAndDatePart(
            @PathVariable(name = "userId") Integer userId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) @Min(value = 0, message = INCORRECT_YEAR_ERROR) Integer year,
            @RequestParam(required = false) @Min(value = 0, message = INCORECT_MONTH_ERROR) @Max(value = 12, message = INCORECT_MONTH_ERROR) Integer month,
            @RequestParam(required = false) @Min(value = 0, message = INCORRECT_SIZE_ERROR) Integer size,
            @RequestParam(required = false) @Min(value = 0, message = INCORRECT_PAGE_ERROR) Integer page) {
        log.info(String.format(FIND_BY_CATEGORY_ID_AND_DATE_INFO, categoryId, year, month));
        Page<ExpenseDTO> expensesPage = expensesService.findByUserIdAndCategoryIdAndDatePart(userId, categoryId, new DatePart(year, month), PageRequest.of(page, size));
        return ResponseEntity.ok(expensesPage.map(expensesAssembler::toModel));
    }
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> save(@Valid @RequestBody ExpenseDTO expenseDTO) {
		log.info(String.format(SAVE_EXPENSE_INFO, expenseDTO.toString()));
		ExpenseDTO addedExpense =  expensesService.save(expenseDTO);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(addedExpense.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ExpenseDTO> updateItem(
			@PathVariable @Min(value = 1, message = INCORRECT_ID_ERROR) Long id,
			@Valid @RequestBody ExpenseDTO expenseDTO){
		log.info(String.format(UPDATE_EXPENSE_INFO, id));	
		return ResponseEntity.ok(expensesService.update(expenseDTO, id));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable
			@Min(value = 1, message = INCORRECT_ID_ERROR) Long id) throws ResourceNotFoundException {
		log.info(String.format(DELETE_EXPENSE_INFO, id));
		expensesService.delete(id);
		return ResponseEntity.noContent().build();
	}
		
}