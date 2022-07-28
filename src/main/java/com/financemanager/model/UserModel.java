package com.financemanager.model;

import org.springframework.hateoas.RepresentationModel;

import com.financemanager.dto.RoleDTO;

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

	private Integer id;
	private String name;
	private String email;
	private RoleDTO roleDTO;
}
