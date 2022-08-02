package com.financemanager.controller;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.financemanager.dto.CategoryDTO;
import com.financemanager.exception.ResourceNotFoundException;
import com.financemanager.model.CategoryModel;
import com.financemanager.service.CategoryService;
import com.financemanager.service.assembler.CategoryModelAssembler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
@PropertySource(value = { "classpath:/messages/category/info.properties" })
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {
    
    private final CategoryService categoryService;
	private final CategoryModelAssembler categoryAssembler;
	
	@Value("${find_by_user_id.info}")
    private String findByUserIdInfo;
    @Value("${find_by_id.info}")
    private String findByIdInfo;
    @Value("${find_all.info}")
    private String findAllInfo;
    @Value("${update.info}")
    private String updateInfo;
    @Value("${save.info}")
    private String saveInfo;
    @Value("${delete.info}")
    private String deleteInfo;
	
	@GetMapping("/{id}")
	@PreAuthorize(" hasAuthority('category:read')") 
	public ResponseEntity<CategoryModel> findCategoryById(@PathVariable Integer id) throws ResourceNotFoundException{
		log.info(findByIdInfo, id);
		return ResponseEntity.ok(categoryAssembler.toModel(categoryService.findById(id)));
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('category:read')") 
	public ResponseEntity<CollectionModel<CategoryModel>> findAllCategories() {
		log.info(findAllInfo);
		List<CategoryDTO> categories = categoryService.findAll();
		return new ResponseEntity<>(
				categoryAssembler.toCollectionModel(categories),
				HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	@PreAuthorize("#userId == authentication.principal.id && hasAuthority('category:read')") 
    public ResponseEntity<CollectionModel<CategoryModel>> findByUserId(@PathVariable Integer userId) {
        log.info(findByUserIdInfo, userId);
        List<CategoryDTO> categories = categoryService.findByUserId(userId);
        return new ResponseEntity<>(
                categoryAssembler.toCollectionModel(categories),
                HttpStatus.OK);
    }
	
	@PostMapping
	@PreAuthorize("hasAuthority('category:write')") 
    public ResponseEntity<?> save(@Valid @RequestBody CategoryDTO categoryDTO) {
        log.info(String.format(saveInfo, categoryDTO.toString()));
        CategoryDTO addedCategory = categoryService.save(categoryDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedCategory.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('category:write')") 
	public ResponseEntity<CategoryDTO> update(
	        @PathVariable Integer id,
			@Valid @RequestBody CategoryDTO categoryDTO){
		log.info(updateInfo + id);	
		return ResponseEntity.ok(categoryService.update(categoryDTO, id));
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id) throws ResourceNotFoundException{
        log.info(deleteInfo, id);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
	
}
