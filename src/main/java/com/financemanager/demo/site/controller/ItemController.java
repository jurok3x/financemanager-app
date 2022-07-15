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
import org.springframework.http.MediaType;
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

import com.financemanager.demo.site.dto.ItemDTO;
import com.financemanager.demo.site.entity.payload.SaveItemRequest;
import com.financemanager.demo.site.entity.projects.DatePartAndCost;
import com.financemanager.demo.site.entity.projects.ProjectNameAndCountAndCost;
import com.financemanager.demo.site.exception.ResourceNotFoundException;
import com.financemanager.demo.site.model.ItemModel;
import com.financemanager.demo.site.service.ItemService;
import com.financemanager.demo.site.service.assembler.ItemModelAssembler;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/api/items")
@AllArgsConstructor
@Log
@SecurityRequirement(name = "bearerAuth")
public class ItemController {
	private static final String GET_MONTH_STATISTICS_INFO = "Handling get month statistics with year %1$d and categoryId %2$d";
    private static final String SAVE_ITEM_INFO = "Handling save new item %s";
    private static final String DELETE_ITEM_INFO = "Handling delete item with id %d";
    private static final String GET_ALL_ACTIVE_YEARS_INFO = "Handling find all active years";
    private static final String GET_ITEMS_COUNT_INFO = "Handling get items count of category with id %1$d and year %2$d and month %3$d";
    private static final String GET_MOST_POPULAR_ITEMS_INFO = "Handling get most popular items of category with id %1$d and year %2$d and month %3$d";
    private static final String FIND_BY_DATE_INFO = "Handling find items of year %1$d and month %2$d";
	private static final String FIND_BY_CATEGORY_ID_AND_DATE_INFO = "Handling find items with category ID %1$d of year %2$d and month %3$d";
    private static final String FIND_BY_ID_INFO = "Handling find item with ID %d";
    private static final String INCORRECT_OFFSET_ERROR = "Offset can not be less then 0";
    private static final String INCORECT_MONTH_ERROR = "Incorect month";
    private static final String INCORRECT_YEAR_ERROR = "Incorrect year";
    private static final String INCORRECT_LIMIT_ERROR = "Incorrect limit";
    private static final String ITEM_ID_NOT_FOUND_ERROR = "Item with ID %d Not Found!";
    private static final String INCORRECT_ID_ERROR = "Id must be greater than or equal to 1";
    private ItemService itemService;
	private ItemModelAssembler itemAssembler;

	@GetMapping("/{id}")
	public ResponseEntity<ItemModel> findById(@PathVariable
			@Min(value = 1, message = INCORRECT_ID_ERROR) Long id) throws ResourceNotFoundException {
		log.info(String.format(FIND_BY_ID_INFO, id));
	    return itemService.findById(id)
				.map(itemAssembler::toModel)
				.map(ResponseEntity::ok)
				.orElseThrow(
						()->new ResourceNotFoundException(String.format(ITEM_ID_NOT_FOUND_ERROR, id)));
	}
	
	@GetMapping
	public ResponseEntity<CollectionModel<ItemModel>> findAll(
			@RequestParam Optional<@Min(value = 0, message = INCORRECT_YEAR_ERROR) Integer> year,
			@RequestParam Optional<@Min(value = 0, message = INCORECT_MONTH_ERROR) @Max(value = 12, message = INCORECT_MONTH_ERROR) Integer> month,
			@RequestParam Optional<@Min(value = 1, message = INCORRECT_LIMIT_ERROR) Integer> limit,
			@RequestParam Optional<@Min(value = 0, message = INCORRECT_OFFSET_ERROR) Integer> offset) {
		log.info(String.format(FIND_BY_DATE_INFO, year.orElse(null), month.orElse(null)));
		List<ItemDTO> items = itemService.findAll(year, month, limit, offset);
		return new ResponseEntity<>(
				itemAssembler.toCollectionModel(items),
				HttpStatus.OK);
	}
	
	@GetMapping(value = {"/category/{categoryId}"})
	public ResponseEntity<CollectionModel<ItemModel>> findByCategoryId(
			@PathVariable Integer categoryId,
			@RequestParam Optional<@Min(value = 0, message = INCORRECT_YEAR_ERROR) Integer> year,
	        @RequestParam Optional<@Min(value = 0, message = INCORECT_MONTH_ERROR) @Max(value = 12, message = INCORECT_MONTH_ERROR) Integer> month,
	        @RequestParam Optional<@Min(value = 1, message = INCORRECT_LIMIT_ERROR) Integer> limit,
			@RequestParam Optional<@Min(value = 0, message = INCORRECT_OFFSET_ERROR) Integer> offset) {
		log.info(String.format(FIND_BY_CATEGORY_ID_AND_DATE_INFO, categoryId, year.orElse(null), month.orElse(null)));
		List<ItemDTO> items = itemService.findByCategoryId(categoryId, year, month, limit, offset);
		return new ResponseEntity<>(
				itemAssembler.toCollectionModel(items),
				HttpStatus.OK);
	}

	@GetMapping("/count/category/{categoryId}")
	public ResponseEntity<Integer> countByCategoryAndDate(
			@PathVariable Integer categoryId,
			@RequestParam Optional<@Min(value = 0, message = INCORRECT_YEAR_ERROR) Integer> year,
			@RequestParam Optional<@Min(value = 0, message = INCORECT_MONTH_ERROR) @Max(value = 12, message = INCORECT_MONTH_ERROR) Integer> month) {
		log.info(String.format(GET_ITEMS_COUNT_INFO, categoryId, year.orElse(null), month.orElse(null)));
		return new ResponseEntity<>(
				itemService.countItemsByCategoryId(categoryId, year, month),
				HttpStatus.OK);
	}
	
	@GetMapping("/popular")
	public List<ProjectNameAndCountAndCost> getMostPopularItems(
			@RequestParam Optional<Integer> categoryId,
			@RequestParam Optional<@Min(value = 0, message = INCORRECT_YEAR_ERROR) Integer> year,
            @RequestParam Optional<@Min(value = 0, message = INCORECT_MONTH_ERROR) @Max(value = 12, message = INCORECT_MONTH_ERROR) Integer> month,
			@RequestParam Optional<@Min(value = 1, message = INCORRECT_LIMIT_ERROR) Integer> limit,
			@RequestParam Optional<@Min(value = 0, message = INCORRECT_OFFSET_ERROR) Integer> offset) {
		log.info(String.format(GET_MOST_POPULAR_ITEMS_INFO, categoryId.orElse(null), year.orElse(null), month.orElse(null)));
		return itemService.getMostPopularItems(categoryId, year, month, limit, offset);
	}
	
	@GetMapping("/years")
	public List<Integer> getActiveYears() {
		log.info(GET_ALL_ACTIVE_YEARS_INFO);
		return itemService.getActiveYears();
	}
	
	@GetMapping("/statistics")
	public List<DatePartAndCost> getStatisticsByMonth(
			@RequestParam Optional<Integer> categoryId,
			@RequestParam Optional<@Min(value = 0, message = INCORRECT_YEAR_ERROR) Integer> year) {
		log.info(String.format(GET_MONTH_STATISTICS_INFO, year.orElse(null), categoryId.orElse(null)));
		return itemService.getStatisticsByMonth(categoryId, year);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> save(@Valid @RequestBody SaveItemRequest request) {
		log.info(String.format(SAVE_ITEM_INFO, request.toString()));
		ItemDTO addedItem =  itemService.save(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(addedItem.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ItemDTO> updateItem(
			@PathVariable @Min(value = 1, message = INCORRECT_ID_ERROR) Long id,
			@Valid @RequestBody SaveItemRequest request){
		log.info("Handling update item with id = " + id);	
		return ResponseEntity.ok(itemService.update(request, id));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable
			@Min(value = 1, message = INCORRECT_ID_ERROR) Long id) throws ResourceNotFoundException {
		log.info(String.format(DELETE_ITEM_INFO, id));
		itemService.delete(id);
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
	
	@RequestMapping(value = "/statistics, /years, /popular, /count/category/{categoryId}, /category/{categoryId}" , method = RequestMethod.OPTIONS)
	ResponseEntity<?> specialOptions() 
    {
         return ResponseEntity
                 .ok()
                 .allow(HttpMethod.GET, HttpMethod.OPTIONS)
                 .build();
    }
		
}
