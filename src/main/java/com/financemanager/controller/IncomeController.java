package com.financemanager.controller;

import com.financemanager.dto.IncomeDTO;
import com.financemanager.entity.utils.DatePart;
import com.financemanager.model.IncomeModel;
import com.financemanager.service.IncomeService;
import com.financemanager.service.assembler.IncomeModelAssembler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
@RequiredArgsConstructor
@Slf4j
@PropertySource(value = { "classpath:/messages/income/info.properties" })
public class IncomeController {
    
    private IncomeService incomeService;
    private IncomeModelAssembler incomeAssembler;
    
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
    public ResponseEntity<IncomeModel> findById(@PathVariable Long id) {
        log.info(findByIdInfo, id);
        return ResponseEntity.ok(incomeAssembler.toModel(incomeService.findById(id)));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<CollectionModel<IncomeModel>> findByUserIdAndCategoryIdAndDatePart(
            @PathVariable Integer userId,
            @RequestParam(required = false) @Min(value = 0) Integer year,
            @RequestParam(required = false) @Min(value = 0) @Max(value = 12) Integer month) {
        log.info(findByUserIdInfo, userId, month, year);
        List<IncomeDTO> incomes = incomeService.findByUserIdAndDatePart(userId, new DatePart(year, month));
        return ResponseEntity.ok(incomeAssembler.toCollectionModel(incomes));
    }
    
    @GetMapping("/page/user/{userId}")
    public ResponseEntity<Page<IncomeModel>> findByUserIdAndCategoryIdAndDatePart(
            @PathVariable Integer userId,
            @RequestParam(required = false) @Min(value = 0) Integer year,
            @RequestParam(required = false) @Min(value = 0) @Max(value = 12) Integer month,
            @RequestParam(required = false) @Min(value = 0) Integer size,
            @RequestParam(required = false) @Min(value = 0) Integer page) {
        log.info(findByUserIdInfo, userId, month, year);
        Page<IncomeDTO> incomesPage = incomeService.findByUserIdAndDatePart(userId, new DatePart(year, month), PageRequest.of(page, size));
        return ResponseEntity.ok(incomesPage.map(incomeAssembler::toModel));
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody IncomeDTO incomeDTO) {
        log.info(saveInfo, incomeDTO.toString());
        IncomeDTO addedIncome = incomeService.save(incomeDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedIncome.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<IncomeModel> update(
            @PathVariable Long id,
            @Valid @RequestBody IncomeDTO incomeDTO) {
        log.info(updateInfo, id);
        return ResponseEntity.ok(incomeAssembler.toModel(incomeService.update(incomeDTO, id)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.info(deleteInfo, id);
        return ResponseEntity.noContent().build();
    }

}
