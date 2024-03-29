package com.financemanager.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Entity
@Table(name = "\"categories\"")
@Data
public class Category implements Comparable<Category>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	@Column(name="name")
	@NotBlank(message = "Name must not be empty")
	private String name;
	@ManyToMany(mappedBy = "categories", cascade = CascadeType.MERGE)
	private Set<User> users;
	
	public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.users = new HashSet<>();
    }
	
	public Category() {
        this.users = new HashSet<>();
    }
	
	@Override
	public int compareTo(Category category) {
		return this.id - category.getId();
	}
	
	public void addUser(User user) {
	    this.users.add(user);
	}
	
	public void removeUser(User user) {
        this.users.remove(user);
    }

}
