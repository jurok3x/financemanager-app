package com.financemanager.service.assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.financemanager.controller.CategoryController;
import com.financemanager.dto.CategoryDTO;
import com.financemanager.model.CategoryModel;

import org.springframework.hateoas.CollectionModel;

@Component
public class CategoryModelAssembler extends RepresentationModelAssemblerSupport<CategoryDTO, CategoryModel> {

	public CategoryModelAssembler() {
		super(CategoryController.class, CategoryModel.class);
	}

	@Override
	public CategoryModel toModel(CategoryDTO entity) {
		CategoryModel categoryModel = instantiateModel(entity);
		
		categoryModel.setId(entity.getId());
		categoryModel.setName(entity.getName());
		
		categoryModel.add(linkTo(
				methodOn(CategoryController.class)
				.findCategoryById(entity.getId()))
				.withSelfRel());
		
		return categoryModel;
	}
	
	@Override
	public CollectionModel<CategoryModel> toCollectionModel(Iterable<? extends CategoryDTO> entities) {
		CollectionModel<CategoryModel> categoriesModel = super.toCollectionModel(entities);
		categoriesModel.add(linkTo(
				methodOn(CategoryController.class).findAllCategories())
				.withSelfRel());
		return categoriesModel;
	}

}
