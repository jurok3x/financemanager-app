package com.financemanager.demo.site.service;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Optional;

import org.springframework.hateoas.CollectionModel;

import com.financemanager.demo.site.controller.CategoryController;
import com.financemanager.demo.site.controller.ItemController;
import com.financemanager.demo.site.entity.Category;
import com.financemanager.demo.site.model.CategoryModel;

@Component
public class CategoryModelAssembler extends RepresentationModelAssemblerSupport<Category, CategoryModel> {

	public CategoryModelAssembler() {
		super(CategoryController.class, CategoryModel.class);
	}

	@Override
	public CategoryModel toModel(Category entity) {
		CategoryModel categoryModel = instantiateModel(entity);
		
		categoryModel.setId(entity.getId());
		categoryModel.setName(entity.getName());
		
		categoryModel.add(linkTo(
				methodOn(CategoryController.class)
				.findCategoryById(entity.getId()))
				.withSelfRel());
		categoryModel.add(linkTo(
				methodOn(ItemController.class)
				.countByCategoryAndDate(entity.getId(), Optional.empty(), Optional.empty()))
				.withRel("items-count"));
		categoryModel.add(linkTo(
				methodOn(ItemController.class)
				.findByCategoryId(entity.getId(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()))
				.withRel("items"));
		
		return categoryModel;
	}
	
	@Override
	public CollectionModel<CategoryModel> toCollectionModel(Iterable<? extends Category> entities) {
		CollectionModel<CategoryModel> categoriesModel = super.toCollectionModel(entities);
		categoriesModel.add(linkTo(
				methodOn(CategoryController.class).findAllCategories())
				.withSelfRel());
		categoriesModel.add(linkTo(
				methodOn(CategoryController.class)
				.getCategoriesAndCost(Optional.empty(), Optional.empty()))
				.withRel("all-categories-cost"));
		categoriesModel.add(linkTo(
				methodOn(CategoryController.class)
				.getCategoriesAndCount(Optional.empty(), Optional.empty()))
				.withRel("all-categories-count"));
		return categoriesModel;
	}

}
