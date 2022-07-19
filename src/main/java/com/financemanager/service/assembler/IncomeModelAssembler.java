package com.financemanager.service.assembler;

import com.financemanager.controller.IncomeController;
import com.financemanager.dto.IncomeDTO;
import com.financemanager.model.IncomeModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class IncomeModelAssembler  extends RepresentationModelAssemblerSupport<IncomeDTO, IncomeModel>{

    public IncomeModelAssembler(Class<?> controllerClass, Class<IncomeModel> resourceType) {
        super(IncomeController.class, IncomeModel.class);
    }
    
    @Autowired
    private UserModelAssembler userAssembler;

    @Override
    public IncomeModel toModel(IncomeDTO entity) {
        IncomeModel incomeModel = instantiateModel(entity);
        incomeModel.setId(entity.getId());
        incomeModel.setName(entity.getName());
        incomeModel.setAmount(entity.getAmount());
        incomeModel.setDate(entity.getDate());
        incomeModel.setUser(userAssembler.toModel(entity.getUserDTO()));
        
        incomeModel.add(linkTo(
                methodOn(IncomeController.class)
                .findById(entity.getId()))
                .withSelfRel());
        return incomeModel;
    }
    
    @Override
    public CollectionModel<IncomeModel> toCollectionModel(Iterable<? extends IncomeDTO> entities) {
        CollectionModel<IncomeModel> incomesModel = super.toCollectionModel(entities);
        incomesModel.add(linkTo(
                methodOn(IncomeController.class)
                .findByUserIdAndCategoryIdAndDatePart(entities.iterator().next().getUserDTO().getId(), null, null))
                .withSelfRel());
        return incomesModel;
    }

}
