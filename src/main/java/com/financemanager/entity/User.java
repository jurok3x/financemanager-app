package com.financemanager.entity;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Entity
@Table(name = "\"users\"")
@Data
public class User {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	@NotBlank(message = "Name must not be empty")
	private String name;
	
	@Column(name="password")
	@NotBlank(message = "Password must not be empty")
	private String password;
	
	@Column(name="email")
	@NotBlank(message = "Email must not be empty")
    @Email(message = "Email must be a valid email address")
	private String email;
	
	@JoinColumn(name="\"role_id\"")
	@NotNull(message = "Role must not be empty")
	@Enumerated(EnumType.ORDINAL)
	private Role role;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
	        name = "\"users_categories\"",
	        joinColumns = @JoinColumn(name = "\"user_id\""),
	        inverseJoinColumns = @JoinColumn(name = "\"category_id\""))
	private Set<Category> categories = new HashSet<>();
	
	public User() {
	    this.categories = new HashSet<>();
	}
	
	public User(Integer id, String name,
            String password,
            String email,
            Role role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.categories = new HashSet<>();
    }
	
	public void addCategory(Category category) {
	    this.categories.add(category);
	}
	
}
