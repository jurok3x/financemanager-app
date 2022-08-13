package com.financemanager.controller;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
import com.financemanager.service.CategoryService;

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
	public ResponseEntity<CategoryDTO> findCategoryById(@PathVariable Integer id) throws ResourceNotFoundException{
		log.info(findByIdInfo, id);
		return ResponseEntity.ok(categoryService.findById(id));
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('category:read')") 
	public ResponseEntity<List<CategoryDTO>> findAllCategories() {
		log.info(findAllInfo);
		return ResponseEntity.ok(categoryService.findAll());
	}
	
	@GetMapping("/user/{userId}")
	@PreAuthorize("#userId == authentication.principal.id && hasAuthority('category:read')") 
    public ResponseEntity<List<CategoryDTO>> findByUserId(@PathVariable Integer userId) {
        log.info(findByUserIdInfo, userId);
        return ResponseEntity.ok(categoryService.findByUserId(userId));
    }
	
	@PostMapping
	@PreAuthorize("hasAuthority('category:write')") 
    public ResponseEntity<?> save(@RequestBody CategoryDTO categoryDTO) {
        log.info(saveInfo, categoryDTO.toString());
        CategoryDTO addedCategory = categoryService.save(categoryDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedCategory.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('category:write')") 
	public ResponseEntity<CategoryDTO> update(@RequestBody CategoryDTO categoryDTO, @PathVariable Integer id){
		log.info(updateInfo, id);	
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
