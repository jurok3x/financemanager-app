package com.financemanager.demo.site.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.financemanager.demo.site.entity.Item;
import com.financemanager.demo.site.entity.projects.DatePartAndCost;
import com.financemanager.demo.site.entity.projects.ProjectNameAndCountAndCost;
import com.financemanager.demo.site.repository.ItemRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DefaultItemService implements ItemService {
	private final ItemRepository itemRepository;
	private final UserService userService;

	@Override
	public Item saveItem(Item item) {
		item.setUser(userService.getContextUser());
		System.out.print("Saving item " + item);
		Item savedItem = itemRepository.save(item);
		System.out.print("Saved item " + savedItem);
		return savedItem;
	}
	

	@Override
	public Optional<Item> findById(Integer id) {
		return itemRepository.findById(id);
	}

	@Override
	public void deleteItem(Integer id) {
		itemRepository.deleteById(id);
	}

	@Override
	public List<Item> findAll(Optional<String> year, Optional<String> month,
		Optional<Integer> limit, Optional<Integer> offset) {	
		String dateString = "%" + year.orElse("") + "-" +
		month.map(monthString->{
			if(monthString.length() == 1) {
				monthString = "0" + monthString;
			}
			return monthString;
		}).orElse("") + "%";
		if(limit.isPresent() || offset.isPresent()) {
			return itemRepository
					.findByUserIdAndDate(userService.getContextUser().getId(), dateString,
							limit.orElse(10), offset.orElse(0));
		} return itemRepository
				.findByUserIdAndDateAll(userService.getContextUser().getId(), dateString);
	}

	@Override
	public List<Item> findByCategory(int categoryId, Optional<String> year, Optional<String> month,
		Optional<Integer> limit, Optional<Integer> offset) {
		String dateString = "%" + year.orElse("") + "-" +
				month.map(monthString->{
					if(monthString.length() == 1) {
						monthString = "0" + monthString;
					}
					return monthString;
				}).orElse("") + "%";
		return itemRepository.findByUserIdAndCategoryIdAndDate(userService.getContextUser().getId(), categoryId,
				dateString, limit.orElse(10), offset.orElse(0));
	}

	@Override
	public Integer countItemsByCategory(int cetegoryId, Optional<String> year, Optional<String> month) {
		String dateString = "%" + year.orElse("") + "-" +
				month.map(monthString->{
					if(monthString.length() == 1) {
						monthString = "0" + monthString;
					}
					return monthString;
				}).orElse("") + "%";
			return itemRepository.countByUserIdAndCategoryIdAndDate(userService.getContextUser().getId(),
					cetegoryId, dateString);	
	}
	
	@Override
	public List<ProjectNameAndCountAndCost> getMostFrequentItems(Optional<Integer> categoryId, Optional<String> year, Optional<String> month,
			Optional<Integer> limit, Optional<Integer> offset) {
		String dateString = "%" + year.orElse("") + "-" +
				month.map(monthString->{
					if(monthString.length() == 1) {
						monthString = "0" + monthString;
					}
					return monthString;
				}).orElse("") + "%";
		if(!categoryId.isPresent()) {
			return itemRepository.getMostFrequentItemsByDate(userService.getContextUser().getId(), dateString, limit.orElse(5), offset.orElse(0));
		}
		return  itemRepository.getMostFrequentItemsByCategoryAndDate(userService.getContextUser().getId(), categoryId.get(),
				dateString, limit.orElse(5), offset.orElse(0));
	}

	@Override
	public List<Integer> getAllYears() {
		return itemRepository.getAllYears(userService.getContextUser().getId());
	}

	@Override
	public List<DatePartAndCost> getStatisticsByMonth(Optional<Integer> categoryId, Optional<String> year) {
		String dateString = "%" + year.orElse("") + "%";
		if(!categoryId.isPresent()) {
			return itemRepository.getMonthStatistics(userService.getContextUser().getId(), dateString);
		}
		return itemRepository.getMonthStatisticsByCategory(userService.getContextUser().getId(), dateString, categoryId.get());
	}


	@Override
	public List<Item> saveAllItems(List<Item> items) {
		return itemRepository.saveAll(items);
	}

}