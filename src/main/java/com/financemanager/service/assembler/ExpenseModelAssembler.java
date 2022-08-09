package com.financemanager.service.assembler;

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

	@Override
	public ExpenseModel toModel(ExpenseDTO entity) {
		ExpenseModel expenseModel = instantiateModel(entity);
		
		expenseModel.setId(entity.getId());
		expenseModel.setName(entity.getName());
		expenseModel.setPrice(entity.getPrice());
		expenseModel.setCategoryId(entity.getCategoryDTO().getId());
		expenseModel.setUserId(entity.getUserDTO().getId());
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
		Integer userId = entities.iterator().hasNext() ? entities.iterator().next().getUserDTO().getId() : null;
		expenseModel.add(linkTo(
				methodOn(ExpensesController.class)
				.findByUserIdAndCategoryIdAndDatePart(userId, null, null, null))
				.withSelfRel());
		return expenseModel;
	}

}
