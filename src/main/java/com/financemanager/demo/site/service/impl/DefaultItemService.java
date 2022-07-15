package com.financemanager.demo.site.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.financemanager.demo.site.dto.ItemDTO;
import com.financemanager.demo.site.entity.payload.SaveItemRequest;
import com.financemanager.demo.site.entity.projects.DatePartAndCost;
import com.financemanager.demo.site.entity.projects.ProjectNameAndCountAndCost;
import com.financemanager.demo.site.exception.ResourceNotFoundException;
import com.financemanager.demo.site.mapper.ItemMapper;
import com.financemanager.demo.site.repository.ItemRepository;
import com.financemanager.demo.site.service.ItemService;
import com.financemanager.demo.site.service.utils.ItemsUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DefaultItemService implements ItemService {
	private static final String ITEM_ID_NOT_FOUND_ERROR = "Item with id - %d, not found";
    private static final int DEFAULT_LIMIT = 10;
    private final ItemRepository itemRepository;
	private final ItemMapper itemMapper;
	
	@Override
	public ItemDTO save(SaveItemRequest request) {
	    ItemDTO itemDTO = new ItemDTO();
        itemDTO.setCategory(request.getCategory());
        itemDTO.setName(request.getName());
        itemDTO.setPrice(request.getPrice());
        itemDTO.setDate(request.getDate());
        itemDTO.setUser(request.getUser());
		return itemMapper.toItemDTO(itemRepository.save(itemMapper.toItem(itemDTO)));
	}
	
	@Override
    public ItemDTO update(SaveItemRequest request, Long id) {
        ItemDTO itemDTO = itemRepository.findById(id).map(itemMapper::toItemDTO).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ITEM_ID_NOT_FOUND_ERROR, id)));
        itemDTO.setCategory(request.getCategory());
        itemDTO.setName(request.getName());
        itemDTO.setPrice(request.getPrice());
        itemDTO.setDate(request.getDate());
        itemDTO.setUser(request.getUser());
        return itemMapper.toItemDTO(itemRepository.save(itemMapper.toItem(itemDTO)));
    }
	
	@Override
	public Optional<ItemDTO> findById(Long id) {
	    ItemDTO itemDTO = itemRepository.findById(id).map(itemMapper::toItemDTO).orElseThrow(
	            () -> new ResourceNotFoundException(String.format(ITEM_ID_NOT_FOUND_ERROR, id)));
		return Optional.ofNullable(itemDTO);
	}

	@Override
	public void delete(Long id) {
	    ItemDTO deletedItem = itemRepository.findById(id).map(itemMapper::toItemDTO).orElseThrow(
	              ()->new ResourceNotFoundException(String.format(ITEM_ID_NOT_FOUND_ERROR, id)));
		itemRepository.deleteById(deletedItem.getId());
	}

	@Override
	public List<ItemDTO> findAll(Optional<Integer> year, Optional<Integer> month,
		Optional<Integer> limit, Optional<Integer> offset) {	
		if(limit.isPresent() || offset.isPresent()) {
			return itemRepository
					.findByUserIdAndDate(ItemsUtils.getContextUser().getId(), ItemsUtils.formatDateString(year, month),
							limit.orElse(10), offset.orElse(0)).stream().map(itemMapper::toItemDTO).collect(Collectors.toList());
		} return itemRepository
				.findByUserIdAndDateAll(ItemsUtils.getContextUser().getId(), ItemsUtils.formatDateString(year, month))
				.stream().map(itemMapper::toItemDTO).collect(Collectors.toList());
	}

	@Override
	public List<ItemDTO> findByCategoryId(Integer categoryId, Optional<Integer> year, Optional<Integer> month,
		Optional<Integer> limit, Optional<Integer> offset) {
		if(limit.isPresent() || offset.isPresent()) {
			return itemRepository.findByUserIdAndCategoryIdAndDate(ItemsUtils.getContextUser().getId(), categoryId,
			        ItemsUtils.formatDateString(year, month), limit.orElse(DEFAULT_LIMIT), offset.orElse(0))
			        .stream().map(itemMapper::toItemDTO).collect(Collectors.toList());
		}
		return itemRepository.findByUserIdAndCategoryIdAndDateAll(ItemsUtils.getContextUser().getId(), categoryId,
		        ItemsUtils.formatDateString(year, month)).stream().map(itemMapper::toItemDTO).collect(Collectors.toList());
	}

	@Override
	public Integer countItemsByCategoryId(Integer cetegoryId, Optional<Integer> year, Optional<Integer> month) {
			return itemRepository.countByUserIdAndCategoryIdAndDate(ItemsUtils.getContextUser().getId(),
					cetegoryId, ItemsUtils.formatDateString(year, month));	
	}
	
	@Override
	public List<ProjectNameAndCountAndCost> getMostPopularItems(Optional<Integer> categoryId, Optional<Integer> year, Optional<Integer> month,
			Optional<Integer> limit, Optional<Integer> offset) {
		if(!categoryId.isPresent()) {
			return itemRepository.getMostFrequentItemsByDate(ItemsUtils.getContextUser().getId(), ItemsUtils.formatDateString(year, month), limit.orElse(5), offset.orElse(0));
		}
		return  itemRepository.getMostFrequentItemsByCategoryAndDate(ItemsUtils.getContextUser().getId(), categoryId.get(),
		        ItemsUtils.formatDateString(year, month), limit.orElse(5), offset.orElse(0));
	}

	@Override
	public List<Integer> getActiveYears() {
		return itemRepository.getAllYears(ItemsUtils.getContextUser().getId());
	}

	@Override
	public List<DatePartAndCost> getStatisticsByMonth(Optional<Integer> categoryId, Optional<Integer> year) {
		if(!categoryId.isPresent()) {
			return itemRepository.getMonthStatistics(ItemsUtils.getContextUser().getId(), ItemsUtils.formatDateString(year, Optional.empty()));
		}
		return itemRepository.getMonthStatisticsByCategory(ItemsUtils.getContextUser().getId(), ItemsUtils.formatDateString(year, Optional.empty()), categoryId.get());
	}

}