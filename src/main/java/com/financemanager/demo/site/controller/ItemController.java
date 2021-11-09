package com.financemanager.demo.site.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.financemanager.demo.site.entity.Item;
import com.financemanager.demo.site.entity.projects.DatePartAndCost;
import com.financemanager.demo.site.entity.projects.ProjectNameAndCountAndCost;
import com.financemanager.demo.site.exception.ResourceNotFoundException;
import com.financemanager.demo.site.model.ItemModel;
import com.financemanager.demo.site.service.ItemModelAssembler;
import com.financemanager.demo.site.service.ItemService;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/api/items")
@AllArgsConstructor
@Log
public class ItemController {
	private ItemService itemService;
	private ItemModelAssembler itemAssembler;

	@GetMapping("/{id}")
	public ResponseEntity<ItemModel> findById(@PathVariable
			@Min(value = 1, message = "Id must be greater than or equal to 1") Integer id) throws ResourceNotFoundException {
		return itemService.findById(id)
				.map(itemAssembler::toModel)
				.map(ResponseEntity::ok)
				.orElseThrow(
						()->new ResourceNotFoundException("Item with ID :" + id +" Not Found!"));
	}
	
	@GetMapping
	public ResponseEntity<CollectionModel<ItemModel>> findAll(
			@RequestHeader("Authorization") String userToken,
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}[0-9]{3}", message = "Incorect year") String> year,
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}|1[0-2]{1}", message = "Incorect month") String> month,
			@RequestParam Optional<@Min(value = 1, message = "Minimum 1 item") Integer> limit,
			@RequestParam Optional<@Min(value = 0, message = "Offset can not be less then 0") Integer> offset) {
		log.info("Handling find items with year = " + year.orElse("All") + " and month = " + month.orElse("All"));
		List<Item> items = itemService.findAll(userToken.substring(7), year, month, limit, offset);
		return new ResponseEntity<>(
				itemAssembler.toCollectionModel(items),
				HttpStatus.OK);
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<CollectionModel<ItemModel>> findAllByUserId(
			@PathVariable
			@Min(value = 1, message = "Id must be greater than or equal to 1") Integer id,
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}[0-9]{3}", message = "Incorect year") String> year,
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}|1[0-2]{1}", message = "Incorect month") String> month,
			@RequestParam Optional<@Min(value = 1, message = "Minimum 1 item") Integer> limit,
			@RequestParam Optional<@Min(value = 0, message = "Offset can not be less then 0") Integer> offset) {
		log.info("Handling find items with year = " + year.orElse("All") + " and month = " + month.orElse("All"));
		List<Item> items = itemService.findAllByUserId(id, year, month, limit, offset);
		return new ResponseEntity<>(
				itemAssembler.toCollectionModel(items),
				HttpStatus.OK);
	}
	
	@GetMapping(value = {"/category/{categoryId}"})
	public ResponseEntity<CollectionModel<ItemModel>> findByCategoryId(
			@RequestHeader("Authorization") String userToken,
			@PathVariable @Min(value = 1, message = "CategoryId must be greater than or equal to 1") 
				@Max(value = 10, message = "CategoryId must be greater than or equal to 10") Integer categoryId,
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}[0-9]{3}", message = "Incorect year") String> year,
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}|1[0-2]{1}", message = "Incorect month") String> month,
			@RequestParam Optional<@Min(value = 1, message = "Minimum 1 item") Integer> limit,
			@RequestParam Optional<@Min(value = 0, message = "Offset can not be less then 0") Integer> offset) {
		log.info("Handling find items in category  with id =  " + categoryId + ". With year = " + year.orElse("All") + " and month = " + month.orElse("All"));
		List<Item> items = itemService.findByCategory(userToken.substring(7), categoryId, year, month, limit, offset);
		return new ResponseEntity<>(
				itemAssembler.toCollectionModel(items),
				HttpStatus.OK);
	}

	@GetMapping("/count/category/{categoryId}")
	public ResponseEntity<Integer> countByCategoryAndDate(
			@RequestHeader("Authorization") String userToken,
			@PathVariable @Min(value = 1, message = "CategoryId must be greater than or equal to 1")
				@Max(value = 10, message = "CategoryId must be greater than or equal to 10")Integer categoryId,
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}[0-9]{3}", message = "Incorect year") String> year,
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}|1[0-2]{1}", message = "Incorect month") String> month) {
		log.info("Handling get items count in category  with id = " + categoryId + ". With year = " + year.orElse("All") + " and month = " + month.orElse("All"));
		return new ResponseEntity<>(
				itemService.countItemsByCategory(userToken.substring(7), categoryId, year, month),
				HttpStatus.OK);
	}
	
	@GetMapping("/popular")
	public List<ProjectNameAndCountAndCost> getMostFrequentItems(
			@RequestHeader("Authorization") String userToken,
			@RequestParam Optional<@Min(value = 1, message = "CategoryId must be greater than or equal to 1")
				@Max(value = 10, message = "CategoryId must be greater than or equal to 10")Integer> categoryId,
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}[0-9]{3}", message = "Incorect year") String> year,
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}|1[0-2]{1}", message = "Incorect month") String> month,
			@RequestParam Optional<@Min(value = 1, message = "Minimum 1 message") Integer> limit,
			@RequestParam Optional<@Min(value = 0, message = "Offset can not be less then 0") Integer> offset) {
		log.info("Handling find most popular items. With year = " + year.orElse("All") + " and month = " + month.orElse("All"));
		return itemService.getMostFrequentItems(userToken.substring(7), categoryId, year, month, limit, offset);
	}
	
	@GetMapping("/years")
	public List<Integer> getAllYears(@RequestHeader("Authorization") String userToken) {
		log.info("Handling find all years.");
		return itemService.getAllYears(userToken.substring(7));
	}
	
	@GetMapping("/statistics")
	public List<DatePartAndCost> getStatisticsByMonth(
			@RequestHeader("Authorization") String userToken,
			@RequestParam Optional<@Min(value = 1, message = "CategoryId must be greater than or equal to 1")
				@Max(value = 10, message = "CategoryId must be greater than or equal to 10") Integer> categoryId,
			@RequestParam Optional<@Pattern(regexp = "[1-9]{1}[0-9]{3}", message = "Incorect year") String> year) {
		log.info("Handling get month statistics. With year = " + year.orElse("All") + " and categoryId = "
			+ ((categoryId.isPresent()) ? categoryId.get() : "All"));
		return itemService.getStatisticsByMonth(userToken.substring(7), categoryId, year);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveItem(@Valid @RequestBody Item item,
			@RequestHeader("Authorization") String userToken) {
		log.info("Handling save item: " + item);
		Item addedItem =  itemService.saveItem(item, userToken.substring(7));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(addedItem.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateItem(
			@RequestHeader("Authorization") String userToken,
			@PathVariable
				@Min(value = 1, message = "Id should be greater than 1") Integer id,
			@Valid @RequestBody Item updatedItem){
		log.info("Handling update item with id = " + id);	
		return itemService.findById(id)
				.map(item->{
					item.setName(updatedItem.getName());
					item.setPrice(updatedItem.getPrice());
					item.setDate(updatedItem.getDate());
					item.setCategory(updatedItem.getCategory());
					item.setUser(updatedItem.getUser());
					itemService.saveItem(item, userToken.substring(7));
			        return ResponseEntity.ok().build(); 
				})
				.orElseGet(() -> {
						Item addedItem =  itemService.saveItem(updatedItem, userToken.substring(7));
						URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				                .path("/{id}")
				                .buildAndExpand(addedItem.getId())
				                .toUri();
				        return ResponseEntity.created(location).build();
				        });
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable
			@Min(value = 1, message = "Id must be greater than or equal to 1") Integer id) throws ResourceNotFoundException {
		log.info("Handling delete item request: " + id);
		Item deletedItem = itemService.findById(id).orElseThrow(
				()->new ResourceNotFoundException("Item with ID :" + id +" Not Found!"));
		itemService.deleteItem(deletedItem.getId());
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
