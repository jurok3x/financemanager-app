package com.financemanager.demo.site.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.financemanager.demo.site.dto.ItemDTO;
import com.financemanager.demo.site.entity.payload.SaveItemRequest;
import com.financemanager.demo.site.entity.projects.DatePartAndCost;
import com.financemanager.demo.site.entity.projects.ProjectNameAndCountAndCost;

@Component
public interface ItemService {
	ItemDTO save(SaveItemRequest request);
	
	ItemDTO update(SaveItemRequest request, Long id);
	
	Optional<ItemDTO> findById(Long id);

    void delete(Long id);

    List<ItemDTO> findAll(Optional<Integer> year, Optional<Integer> month, Optional<Integer> limit, Optional<Integer> offset);
    
    List<ItemDTO> findByCategoryId(Integer categoryId, Optional<Integer> year, Optional<Integer> month, Optional<Integer> limit, Optional<Integer> offset);
    
    Integer countItemsByCategoryId(Integer cetegoryId, Optional<Integer> year, Optional<Integer> month);
    
    List<ProjectNameAndCountAndCost>getMostPopularItems(Optional<Integer> categoryId, 
    		Optional<Integer> year, Optional<Integer> month, Optional<Integer> limit, Optional<Integer> offset);
    
    List<Integer> getActiveYears();
    
    List<DatePartAndCost> getStatisticsByMonth(Optional<Integer> categoryId, Optional<Integer> year);
}
