package com.financemanager.service.assembler;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.financemanager.controller.UserController;
import com.financemanager.dto.UserDTO;
import com.financemanager.model.UserModel;

import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<UserDTO, UserModel> {

	public UserModelAssembler() {
		super(UserController.class, UserModel.class);
	}

	@Override
	public UserModel toModel(UserDTO entity) {
		UserModel userModel = instantiateModel(entity);
		
		userModel.setId(entity.getId());
		userModel.setName(entity.getName());
		userModel.setEmail(entity.getEmail());
		userModel.setRoleDTO(entity.getRoleDTO());
		
		userModel.add(linkTo(
				methodOn(UserController.class)
				.findById(entity.getId()))
				.withSelfRel());
		
		return userModel;
	}
	
	@Override
	public CollectionModel<UserModel> toCollectionModel(Iterable<? extends UserDTO> entities) {
		CollectionModel<UserModel> usersModel = super.toCollectionModel(entities);
		usersModel.add(linkTo(
				methodOn(UserController.class)
				.findAllUsers())
				.withSelfRel());
		return usersModel;
	}

}
