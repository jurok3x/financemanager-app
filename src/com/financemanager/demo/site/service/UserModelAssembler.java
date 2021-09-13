package com.financemanager.demo.site.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.stereotype.Component;

import com.financemanager.demo.site.controller.UserController;
import com.financemanager.demo.site.entity.User;
import com.financemanager.demo.site.model.UserModel;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {

	public UserModelAssembler() {
		super(UserController.class, UserModel.class);
	}
	
	@Autowired
	RoleModelAssembler roleAssembler;

	@Override
	public UserModel toModel(User entity) {
		UserModel userModel = instantiateModel(entity);
		
		userModel.setId(entity.getId());
		userModel.setLogin(entity.getLogin());
		userModel.setName(entity.getName());
		userModel.setEmail(entity.getEmail());
		userModel.setRole(roleAssembler.toModel(entity.getRole()));
		
		userModel.add(linkTo(
				methodOn(UserController.class)
				.findById(entity.getId()))
				.withSelfRel());
		
		return userModel;
	}
	
	@Override
	public CollectionModel<UserModel> toCollectionModel(Iterable<? extends User> entities) {
		CollectionModel<UserModel> usersModel = super.toCollectionModel(entities);
		usersModel.add(linkTo(
				methodOn(UserController.class)
				.findAllUsers())
				.withSelfRel());
		return usersModel;
	}

}
