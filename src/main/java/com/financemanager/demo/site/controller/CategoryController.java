package com.financemanager.demo.site.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.financemanager.demo.site.dto.CategoryDTO;
import com.financemanager.demo.site.entity.payload.SaveCategoryRequest;
import com.financemanager.demo.site.entity.projects.ProjectCategoryAndCost;
import com.financemanager.demo.site.entity.projects.ProjectCategoryAndCount;
import com.financemanager.demo.site.exception.ResourceNotFoundException;
import com.financemanager.demo.site.model.CategoryModel;
import com.financemanager.demo.site.service.CategoryService;
import com.financemanager.demo.site.service.assembler.CategoryModelAssembler;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
@Log
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {
	
	private static final String FIND_BY_ID_INFO = "Handling find caegory with id %d";
    private static final String FIND_ALL_INFO = "Handling find all caegories request";
    private static final String UPDATE_CAEGORY_INFO = "Handling update caegory with id %d";
    private static final String SAVE_CATEGORY_INFO = "Handling save category %s";
    private static final String FIND_CAEGORIES_AND_COUNT_INFO = "Handling find caegories with their count";
    private static final String FIND_CAEGORIES_AND_COST_INFO = "Handling find caegories with their cost";
    private static final String INCORRECT_MONTH_ERROR = "Incorrect month";
    private static final String INCORRECT_YEAR_ERROR = "Incorrect year";
    private static final String ID_NOT_FOUND_ERROR = "Category with ID : %d Not Found!";
    private static final String INCORRECT_ID_ERROR = "Id should be greater than 1";
    private final CategoryService categoryService;
	private final CategoryModelAssembler categoryAssembler;
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryModel> findCategoryById(@PathVariable
			@Min(value = 1, message = INCORRECT_ID_ERROR) Integer id) throws ResourceNotFoundException{
		log.info(String.format(FIND_BY_ID_INFO, id));
		return categoryService.findById(id)
				.map(categoryAssembler::toModel)
				.map(ResponseEntity::ok)
				.orElseThrow(()->new ResourceNotFoundException(String.format(ID_NOT_FOUND_ERROR, id)));
	}
	
	@GetMapping
	public ResponseEntity<CollectionModel<CategoryModel>> findAllCategories() {
		log.info(FIND_ALL_INFO);
		List<CategoryDTO> categories = categoryService.findAll();
		return new ResponseEntity<>(
				categoryAssembler.toCollectionModel(categories),
				HttpStatus.OK);
	}
	
	@GetMapping("/cost")
	public List<ProjectCategoryAndCost> getCategoriesAndCost(
			@RequestParam Optional<@Min(value = 1, message = INCORRECT_YEAR_ERROR) Integer> year,
			@RequestParam Optional<@Min(value = 1, message = INCORRECT_MONTH_ERROR)
            @Max(value = 12, message = INCORRECT_MONTH_ERROR) Integer> month) {
		log.info(FIND_CAEGORIES_AND_COST_INFO);
		return categoryService.getCategoriesAndCost(year, month);
	}
	
	@GetMapping("/count")
	public List<ProjectCategoryAndCount> getCategoriesAndCount(
			@RequestParam Optional<@Min(value = 1, message = INCORRECT_YEAR_ERROR) Integer> year,
			@RequestParam Optional<@Min(value = 1, message = INCORRECT_MONTH_ERROR)
			  @Max(value = 12, message = INCORRECT_MONTH_ERROR) Integer> month) {
		log.info(FIND_CAEGORIES_AND_COUNT_INFO);
		return categoryService.getCategoriesAndCount(year, month);
	}
	
	@PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody SaveCategoryRequest request) {
        log.info(String.format(SAVE_CATEGORY_INFO, request.toString()));
        CategoryDTO addedCategory = categoryService.save(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedCategory.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
	
	@PutMapping("/{id}")
	public ResponseEntity<CategoryDTO> update(
	        @PathVariable @Min(value = 1, message = INCORRECT_ID_ERROR) Integer id,
			@Valid @RequestBody SaveCategoryRequest request){
		log.info(UPDATE_CAEGORY_INFO + id);	
		return ResponseEntity.ok(categoryService.update(request, id));
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Min(value = 1, message = INCORRECT_ID_ERROR) Integer id) throws ResourceNotFoundException{
        log.info("Handling delete category request: " + id);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
	
	@RequestMapping(value = "/" , method = RequestMethod.OPTIONS)
	ResponseEntity<?> collectionOptions() 
    {
         return ResponseEntity
                 .ok()
                 .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                 .build();
    }
	
	@RequestMapping(value = "/{id}" , method = RequestMethod.OPTIONS)
	ResponseEntity<?> singularOptions() 
    {
         return ResponseEntity
                 .ok()
                 .allow(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE , HttpMethod.OPTIONS)
                 .build();
    }
	
	@RequestMapping(value = "/cost, /count" , method = RequestMethod.OPTIONS)
	ResponseEntity<?> specialOptions() 
    {
         return ResponseEntity
                 .ok()
                 .allow(HttpMethod.GET, HttpMethod.OPTIONS)
                 .build();
    }
}
