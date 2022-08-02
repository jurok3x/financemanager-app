package com.financemanager.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

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
	@ManyToOne
	@NotNull(message = "Role must not be empty")
	private Role role;
	
	@ManyToMany
	@JoinTable(
	        name = "\"users_categories\"",
	        joinColumns = @JoinColumn(name = "\"user_id\""),
	        inverseJoinColumns = @JoinColumn(name = "\"category_id\""))
	private List<Category> categories;
	
	public User() {
	    this.categories = new ArrayList<>();
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
        this.categories = new ArrayList<>();
    }
	
	public void addCategory(Category category) {
	    this.categories.add(category);
	}
	
	public void removeCategory(Category category) {
        this.categories.remove(category);
    }
	
}
