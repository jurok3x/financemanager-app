package com.financemanager.service.assembler;

import com.financemanager.controller.IncomeController;
import com.financemanager.dto.IncomeDTO;
import com.financemanager.model.IncomeModel;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class IncomeModelAssembler  extends RepresentationModelAssemblerSupport<IncomeDTO, IncomeModel>{

    public IncomeModelAssembler() {
        super(IncomeController.class, IncomeModel.class);
    }

    @Override
    public IncomeModel toModel(IncomeDTO entity) {
        IncomeModel incomeModel = instantiateModel(entity);
        incomeModel.setId(entity.getId());
        incomeModel.setName(entity.getName());
        incomeModel.setAmount(entity.getAmount());
        incomeModel.setDate(entity.getDate());
        incomeModel.setUserId(entity.getUserId());
        
        incomeModel.add(linkTo(
                methodOn(IncomeController.class)
                .findById(entity.getId()))
                .withSelfRel());
        return incomeModel;
    }
    
    @Override
    public CollectionModel<IncomeModel> toCollectionModel(Iterable<? extends IncomeDTO> entities) {
        CollectionModel<IncomeModel> incomesModel = super.toCollectionModel(entities);
        int userId = entities.iterator().hasNext() ? entities.iterator().next().getUserId() : null;
        incomesModel.add(linkTo(
                methodOn(IncomeController.class)
                .findByUserIdAndCategoryIdAndDatePart(userId, null, null))
                .withSelfRel());
        return incomesModel;
    }

}
