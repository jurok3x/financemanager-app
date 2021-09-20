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
	public Item saveItem(Item item, String userToken) {
		item.setUser(userService.getUserFromToken(userToken));
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
	public List<Item> findAll(String userToken, Optional<String> year, Optional<String> month,
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
					.findByUserIdAndDate(userService.getUserFromToken(userToken).getId(), dateString,
							limit.orElse(10), offset.orElse(0));
		} return itemRepository
				.findByUserIdAndDateAll(userService.getUserFromToken(userToken).getId(), dateString);
	}

	@Override
	public List<Item> findByCategory(String userToken, int categoryId, Optional<String> year, Optional<String> month,
		Optional<Integer> limit, Optional<Integer> offset) {
		String dateString = "%" + year.orElse("") + "-" +
				month.map(monthString->{
					if(monthString.length() == 1) {
						monthString = "0" + monthString;
					}
					return monthString;
				}).orElse("") + "%";
		return itemRepository.findByUserIdAndCategoryIdAndDate(userService.getUserFromToken(userToken).getId(), categoryId,
				dateString, limit.orElse(10), offset.orElse(0));
	}

	@Override
	public Integer countItemsByCategory(String userToken, int cetegoryId, Optional<String> year, Optional<String> month) {
		String dateString = "%" + year.orElse("") + "-" +
				month.map(monthString->{
					if(monthString.length() == 1) {
						monthString = "0" + monthString;
					}
					return monthString;
				}).orElse("") + "%";
			return itemRepository.countByUserIdAndCategoryIdAndDate(userService.getUserFromToken(userToken).getId(),
					cetegoryId, dateString);	
	}
	
	@Override
	public List<ProjectNameAndCountAndCost> getMostFrequentItems(String userToken, Optional<Integer> categoryId, Optional<String> year, Optional<String> month,
			Optional<Integer> limit, Optional<Integer> offset) {
		String dateString = "%" + year.orElse("") + "-" +
				month.map(monthString->{
					if(monthString.length() == 1) {
						monthString = "0" + monthString;
					}
					return monthString;
				}).orElse("") + "%";
		if(!categoryId.isPresent()) {
			return itemRepository.getMostFrequentItemsByDate(userService.getUserFromToken(userToken).getId(), dateString, limit.orElse(5), offset.orElse(0));
		}
		return  itemRepository.getMostFrequentItemsByCategoryAndDate(userService.getUserFromToken(userToken).getId(), categoryId.get(),
				dateString, limit.orElse(5), offset.orElse(0));
	}

	@Override
	public List<Integer> getAllYears(String userToken) {
		return itemRepository.getAllYears(userService.getUserFromToken(userToken).getId());
	}

	@Override
	public List<DatePartAndCost> getStatisticsByMonth(String userToken, Optional<Integer> categoryId, Optional<String> year) {
		String dateString = "%" + year.orElse("") + "%";
		if(!categoryId.isPresent()) {
			return itemRepository.getMonthStatistics(userService.getUserFromToken(userToken).getId(), dateString);
		}
		return itemRepository.getMonthStatisticsByCategory(userService.getUserFromToken(userToken).getId(), dateString, categoryId.get());
	}


	@Override
	public List<Item> saveAllItems(List<Item> items) {
		return itemRepository.saveAll(items);
	}

}