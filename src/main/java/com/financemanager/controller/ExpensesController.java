package com.financemanager.controller;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.financemanager.dto.ExpenseDTO;
import com.financemanager.entity.utils.DatePart;
import com.financemanager.exception.ResourceNotFoundException;
import com.financemanager.model.ExpenseModel;
import com.financemanager.service.ExpenseService;
import com.financemanager.service.assembler.ExpenseModelAssembler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Slf4j
@PropertySource(value = { "classpath:/messages/expense/info.properties" })
@SecurityRequirement(name = "bearerAuth")
public class ExpensesController {

    private final ExpenseService expensesService;
    private final ExpenseModelAssembler expensesAssembler;

    @Value("${update.info}")
    private String updateInfo;
    @Value("${save.info}")
    private String saveInfo;
    @Value("${delete.info}")
    private String deleteInfo;
    @Value("${find_by_user_id_and_category_id_and_datepart.info}")
    private String findByUserIdAndCategoryIdAndDatePartInfo;
    @Value("${find_by_id.info}")
    private String findByIdInfo;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('expense:read')")
    public ResponseEntity<ExpenseModel> findById(@PathVariable Long id) throws ResourceNotFoundException {
        log.info(findByIdInfo, id);
        return ResponseEntity.ok(expensesAssembler.toModel(expensesService.findById(id)));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id && hasAuthority('expense:read')") 
    public ResponseEntity<CollectionModel<ExpenseModel>> findByUserIdAndCategoryIdAndDatePart(
            @PathVariable(name = "userId") Integer userId, @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) @Min(value = 0) Integer year,
            @RequestParam(required = false) @Min(value = 0) @Max(value = 12) Integer month) {
        log.info(findByUserIdAndCategoryIdAndDatePartInfo, userId, categoryId, year, month);
        List<ExpenseDTO> items = expensesService.findByUserIdAndCategoryIdAndDatePart(userId, categoryId,
                new DatePart(year, month));
        return new ResponseEntity<>(expensesAssembler.toCollectionModel(items), HttpStatus.OK);
    }

    @GetMapping("/page/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id && hasAuthority('expense:read')") 
    public ResponseEntity<Page<ExpenseModel>> findByUserIdAndCategoryIdAndDatePart(
            @PathVariable(name = "userId") Integer userId, @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) @Min(value = 0) Integer year,
            @RequestParam(required = false) @Min(value = 0) @Max(value = 12) Integer month,
            @RequestParam(required = false) @Min(value = 0) Integer size,
            @RequestParam(required = false) @Min(value = 0) Integer page) {
        log.info(findByUserIdAndCategoryIdAndDatePartInfo, userId, categoryId, year, month);
        Page<ExpenseDTO> expensesPage = expensesService.findByUserIdAndCategoryIdAndDatePart(userId, categoryId,
                new DatePart(year, month), PageRequest.of(page, size));
        return ResponseEntity.ok(expensesPage.map(expensesAssembler::toModel));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('expense:write')")
    public ResponseEntity<?> save(@Valid @RequestBody ExpenseDTO expenseDTO) {
        log.info(saveInfo, expenseDTO.toString());
        ExpenseDTO addedExpense = expensesService.save(expenseDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(addedExpense.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('expense:write')")
    public ResponseEntity<ExpenseDTO> updateItem(@PathVariable Long id, @Valid @RequestBody ExpenseDTO expenseDTO) {
        log.info(updateInfo, id);
        return ResponseEntity.ok(expensesService.update(expenseDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) throws ResourceNotFoundException {
        log.info(deleteInfo, id);
        expensesService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
