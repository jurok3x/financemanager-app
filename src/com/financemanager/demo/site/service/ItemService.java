package com.financemanager.demo.site.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.financemanager.demo.site.entity.Item;
import com.financemanager.demo.site.entity.projects.DatePartAndCost;
import com.financemanager.demo.site.entity.projects.ProjectNameAndCountAndCost;

@Component
public interface ItemService {
	Item saveItem(Item item);
	
	List<Item> saveAllItems(List<Item> items);
	
	Optional<Item> findById(Integer id);

    void deleteItem(Integer id);

    List<Item> findAll(Optional<String> year, Optional<String> month, Optional<Integer> limit, Optional<Integer> offset);
    
    List<Item> findByCategory(int categoryId, Optional<String> year, Optional<String> month, Optional<Integer> limit, Optional<Integer> offset);
    
    Integer countItemsByCategory(int cetegoryId, Optional<String> year, Optional<String> month);
    
    List<ProjectNameAndCountAndCost>getMostFrequentItems(Optional<Integer> categoryId, 
    		Optional<String> year, Optional<String> month, Optional<Integer> limit, Optional<Integer> offset);
    
    List<Integer> getAllYears();
    
    List<DatePartAndCost> getStatisticsByMonth(Optional<Integer> categoryId, Optional<String> year);
}
