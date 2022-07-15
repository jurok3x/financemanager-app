package com.financemanager.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
import com.financemanager.entity.payload.SaveExpenseRequest;
import com.financemanager.entity.utils.DatePart;
import com.financemanager.exception.ResourceNotFoundException;
import com.financemanager.model.ExpenseModel;
import com.financemanager.service.ExpensesService;
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
    private static final String INCORRECT_OFFSET_ERROR = "Offset can not be less then 0";
    private static final String INCORECT_MONTH_ERROR = "Incorect month";
    private static final String INCORRECT_YEAR_ERROR = "Incorrect year";
    private static final String INCORRECT_LIMIT_ERROR = "Incorrect limit";
    private static final String EXPENSE_ID_NOT_FOUND_ERROR = "Expense with ID %d Not Found!";
    private static final String INCORRECT_ID_ERROR = "Id must be greater than or equal to 1";
    private ExpensesService expensesService;
	private ExpenseModelAssembler expensesAssembler;

	@GetMapping("/{id}")
	public ResponseEntity<ExpenseModel> findById(@PathVariable
			@Min(value = 1, message = INCORRECT_ID_ERROR) Long id) throws ResourceNotFoundException {
		log.info(String.format(FIND_BY_ID_INFO, id));
	    return expensesService.findById(id)
				.map(expensesAssembler::toModel)
				.map(ResponseEntity::ok)
				.orElseThrow(
						()->new ResourceNotFoundException(String.format(EXPENSE_ID_NOT_FOUND_ERROR, id)));
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<CollectionModel<ExpenseModel>> findByUserId(
	        @PathVariable(name = "userId") Integer userId,
	        @RequestParam(required = false) Integer categoryId,
			@RequestParam(required = false) @Min(value = 0, message = INCORRECT_YEAR_ERROR) Integer year,
	        @RequestParam(required = false) @Min(value = 0, message = INCORECT_MONTH_ERROR) @Max(value = 12, message = INCORECT_MONTH_ERROR) Integer month,
	        @RequestParam(required = false) @Min(value = 1, message = INCORRECT_LIMIT_ERROR) Integer limit,
			@RequestParam Optional<@Min(value = 0, message = INCORRECT_OFFSET_ERROR) Integer> offset) {
		log.info(String.format(FIND_BY_CATEGORY_ID_AND_DATE_INFO, categoryId, year, month));
		List<ExpenseDTO> items = expensesService.findByUserId(userId, categoryId, new DatePart(year, month));
		return new ResponseEntity<>(
				expensesAssembler.toCollectionModel(items),
				HttpStatus.OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> save(@Valid @RequestBody SaveExpenseRequest request) {
		log.info(String.format(SAVE_EXPENSE_INFO, request.toString()));
		ExpenseDTO addedExpense =  expensesService.save(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(addedExpense.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ExpenseDTO> updateItem(
			@PathVariable @Min(value = 1, message = INCORRECT_ID_ERROR) Long id,
			@Valid @RequestBody SaveExpenseRequest request){
		log.info(String.format(UPDATE_EXPENSE_INFO, id));	
		return ResponseEntity.ok(expensesService.update(request, id));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable
			@Min(value = 1, message = INCORRECT_ID_ERROR) Long id) throws ResourceNotFoundException {
		log.info(String.format(DELETE_EXPENSE_INFO, id));
		expensesService.delete(id);
		return ResponseEntity.noContent().build();
	}
		
}
