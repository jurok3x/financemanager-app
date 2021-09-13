package com.financemanager.demo.site.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
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

import com.financemanager.demo.site.entity.Category;
import com.financemanager.demo.site.entity.projects.ProjectCategoryAndCost;
import com.financemanager.demo.site.entity.projects.ProjectCategoryAndCount;
import com.financemanager.demo.site.exception.ResourceNotFoundException;
import com.financemanager.demo.site.model.CategoryModel;
import com.financemanager.demo.site.service.CategoryModelAssembler;
import com.financemanager.demo.site.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
@Log
public class CategoryController {
	
	private final CategoryService categoryService;
	private final CategoryModelAssembler categoryAssembler;
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryModel> findCategoryById(@PathVariable
			@Min(value = 1, message = "Id should be greater than 1") Integer id) throws ResourceNotFoundException{
		log.info("Handling find caegory with id = " + id);
		return categoryService.findById(id)
				.map(categoryAssembler::toModel)
				.map(ResponseEntity::ok)
				.orElseThrow(()->new ResourceNotFoundException("Category with ID :" + id + " Not Found!"));
	}
	
	@GetMapping
	public ResponseEntity<CollectionModel<CategoryModel>> findAllCategories() {
		log.info("Handling find all caegories request");
		List<Category> categories = categoryService.findAll();
		return new ResponseEntity<>(
				categoryAssembler.toCollectionModel(categories),
				HttpStatus.OK);
	}
	
	@GetMapping("/cost")
	public List<ProjectCategoryAndCost> getCategoriesAndCost(
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}[0-9]{3}", message = "Incorect year") String> year,
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}|1[0-2]{1}", message = "Incorect month") String> month) {
		log.info("Handling find caegories with their cost");
		return categoryService.getCategoriesAndCost(year, month);
	}
	
	@GetMapping("/count")
	public List<ProjectCategoryAndCount> getCategoriesAndCount(
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}[0-9]{3}", message = "Incorect year") String> year,
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}|1[0-2]{1}", message = "Incorect month") String> month) {
		log.info("Handling find caegories with their count");
		return categoryService.getCategoriesAndCount(year, month);
	}
	
	@PostMapping
    public ResponseEntity<?> saveCategory(@Valid @RequestBody Category category) {
        log.info("Handling save category: " + category);
        Category addedCategory = categoryService.saveCategory(category);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedCategory.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable
			@Min(value = 1, message = "Id should be greater than 1") Integer id,
			@Valid @RequestBody Category updatedCategory){
		log.info("Handling update caegory with id = " + id);	
		return categoryService.findById(id)
				.map(category->{
					category.setId(updatedCategory.getId());
					category.setName(updatedCategory.getName());
					categoryService.saveCategory(category);
			        return ResponseEntity.ok().build(); 
				})
				.orElseGet(() -> {
						updatedCategory.setId(id);
						Category addedCategory =  categoryService.saveCategory(updatedCategory);
						URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				                .path("/{id}")
				                .buildAndExpand(addedCategory.getId())
				                .toUri();
				        return ResponseEntity.created(location).build();
				        });
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable
    		@Min(value = 1, message = "Id should be greater than 1") Integer id) throws ResourceNotFoundException{
        log.info("Handling delete category request: " + id);
        Category category = categoryService.findById(id)
        		.orElseThrow(()->new ResourceNotFoundException("Category with ID :" + id + " Not Found!"));
        categoryService.deleteCategory(category.getId());
        return ResponseEntity.noContent().build();
    }
}
