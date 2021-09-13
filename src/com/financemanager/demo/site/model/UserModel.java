package com.financemanager.demo.site.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class UserModel extends RepresentationModel<UserModel>{

	private int id;
	private String name;
	private String login;
	private String email;
	private RoleModel role;
}
