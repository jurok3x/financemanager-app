package com.financemanager.demo.site.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.financemanager.demo.site.entity.Category;
import com.financemanager.demo.site.entity.projects.ProjectCategoryAndCost;
import com.financemanager.demo.site.entity.projects.ProjectCategoryAndCount;
import com.financemanager.demo.site.repository.CategoryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DefaultCategoryService implements CategoryService{
	private final CategoryRepository categoryRepository;
	private final UserService userService;
	
@Override
public Category saveCategory(Category category) {
	return categoryRepository.save(category);
}

@Override
public void deleteCategory(Integer catId) {
	categoryRepository.deleteById(catId);
}

@Override
public List<Category> findAll() {
	return categoryRepository.findAll()
			.stream()
			.collect(Collectors.toList());
}

@Override
public Optional<Category> findById(Integer id) {
	return categoryRepository.findById(id);
}

@Override
public List<ProjectCategoryAndCost> getCategoriesAndCost(Optional<String> year, Optional<String> month) {
	String dateString = "%" + year.orElse("") + "-" +
			month.map(monthString->{
				if(monthString.length() == 1) {
					monthString = "0" + monthString;
				}
				return monthString;
			}).orElse("") + "%";
	return categoryRepository.getCategoriesAndCostByDate(userService.getContextUser().getId(), dateString);
}

@Override
public List<ProjectCategoryAndCount> getCategoriesAndCount(Optional<String> year, Optional<String> month) {
	String dateString = "%" + year.orElse("") + "-" +
			month.map(monthString->{
				if(monthString.length() == 1) {
					monthString = "0" + monthString;
				}
				return monthString;
			}).orElse("") + "%";
	return categoryRepository.getCategoriesAndCountByDate(userService.getContextUser().getId(), dateString);
}
}
