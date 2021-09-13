package com.financemanager.demo.site.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Optional;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.financemanager.demo.site.controller.ItemController;
import com.financemanager.demo.site.entity.Item;
import com.financemanager.demo.site.model.ItemModel;

@Component
public class ItemModelAssembler extends RepresentationModelAssemblerSupport<Item, ItemModel> {

	public ItemModelAssembler() {
		super(ItemController.class, ItemModel.class);
	}
	
	@Autowired
	private CategoryModelAssembler categoryAssembler;
	
	@Autowired
	private UserModelAssembler userAssembler;

	@Override
	public ItemModel toModel(Item entity) {
		ItemModel itemModel = instantiateModel(entity);
		
		itemModel.setId(entity.getId());
		itemModel.setName(entity.getName());
		itemModel.setPrice(entity.getPrice());
		itemModel.setCategory(categoryAssembler.toModel(entity.getCategory()));
		itemModel.setUser(userAssembler.toModel(entity.getUser()));
		itemModel.setDate(entity.getDate());
		
		itemModel.add(linkTo(
				methodOn(ItemController.class)
				.findById(entity.getId()))
				.withSelfRel());		
		return itemModel;
	}
	
	@Override
	public CollectionModel<ItemModel> toCollectionModel(Iterable<? extends Item> entities) {
		CollectionModel<ItemModel> itemsModel = super.toCollectionModel(entities);
		itemsModel.add(linkTo(
				methodOn(ItemController.class)
				.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()))
				.withSelfRel());
		itemsModel.add(linkTo(
				methodOn(ItemController.class)
				.getAllYears())
				.withRel("years"));
		itemsModel.add(linkTo(
				methodOn(ItemController.class)
				.getMostFrequentItems(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()))
				.withRel("popular"));
		itemsModel.add(linkTo(
				methodOn(ItemController.class)
				.getStatisticsByMonth(Optional.empty(), Optional.empty()))
				.withRel("all-month-statistic"));
		return itemsModel;
	}

}
