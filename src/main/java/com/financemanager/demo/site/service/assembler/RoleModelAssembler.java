package com.financemanager.demo.site.service.assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;

import com.financemanager.demo.site.controller.RoleController;
import com.financemanager.demo.site.controller.UserController;
import com.financemanager.demo.site.dto.RoleDTO;
import com.financemanager.demo.site.model.RoleModel;

@Component
public class RoleModelAssembler extends RepresentationModelAssemblerSupport<RoleDTO, RoleModel> {

	public RoleModelAssembler() {
		super(RoleController.class, RoleModel.class);
	}

	@Override
	public RoleModel toModel(RoleDTO entity) {
		RoleModel roleModel = instantiateModel(entity);
		roleModel.setId(entity.getId());
		roleModel.setName(entity.getName());
		
		roleModel.add(linkTo(
				methodOn(RoleController.class)
				.findRoleById(entity.getId()))
				.withSelfRel());
		roleModel.add(linkTo(
				methodOn(UserController.class)
				.findByRoleId(entity.getId()))
				.withRel("users"));
		return roleModel;
	}
	
	@Override
	public CollectionModel<RoleModel> toCollectionModel(Iterable<? extends RoleDTO> entities) {
		CollectionModel<RoleModel> rolesModel =  super.toCollectionModel(entities);
		rolesModel.add(linkTo(
				methodOn(RoleController.class)
				.findAllRoles())
				.withSelfRel());
		return rolesModel;
	}

}
