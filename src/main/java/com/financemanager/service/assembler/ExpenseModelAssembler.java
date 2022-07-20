package com.financemanager.service.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.financemanager.controller.ExpensesController;
import com.financemanager.dto.ExpenseDTO;
import com.financemanager.model.ExpenseModel;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ExpenseModelAssembler extends RepresentationModelAssemblerSupport<ExpenseDTO, ExpenseModel> {

	public ExpenseModelAssembler() {
		super(ExpensesController.class, ExpenseModel.class);
	}
	
	@Autowired
	private CategoryModelAssembler categoryAssembler;
	
	@Autowired
	private UserModelAssembler userAssembler;

	@Override
	public ExpenseModel toModel(ExpenseDTO entity) {
		ExpenseModel expenseModel = instantiateModel(entity);
		
		expenseModel.setId(entity.getId());
		expenseModel.setName(entity.getName());
		expenseModel.setPrice(entity.getPrice());
		expenseModel.setCategory(categoryAssembler.toModel(entity.getCategoryDTO()));
		expenseModel.setUser(userAssembler.toModel(entity.getUserDTO()));
		expenseModel.setDate(entity.getDate());
		
		expenseModel.add(linkTo(
				methodOn(ExpensesController.class)
				.findById(entity.getId()))
				.withSelfRel());		
		return expenseModel;
	}
	
	@Override
	public CollectionModel<ExpenseModel> toCollectionModel(Iterable<? extends ExpenseDTO> entities) {
		CollectionModel<ExpenseModel> expenseModel = super.toCollectionModel(entities);
		int userId = entities.iterator().hasNext() ? entities.iterator().next().getUserDTO().getId() : null;
		expenseModel.add(linkTo(
				methodOn(ExpensesController.class)
				.findByUserIdAndCategoryIdAndDatePart(userId, null, null, null))
				.withSelfRel());
		return expenseModel;
	}

}
