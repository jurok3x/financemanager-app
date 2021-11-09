package com.financemanager.demo.site.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "\"user_table\"")
@Data
@NoArgsConstructor
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	@NotBlank(message = "Name must not be empty")
	private String name;
	
	@Column(name="login")
	@NotBlank(message = "Login must not be empty")
	private String login; 
	
	@Column(name="password")
	@NotBlank(message = "Password must not be empty")
	private String password;
	
	@Column(name="email")
	@NotBlank(message = "Email must not be empty")
    @Email(message = "Email must be a valid email address")
	private String email;
	
	@JoinColumn(name="\"role_id\"")
	@OneToOne
	private Role role;
	
}
