package com.financemanager.demo.site.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "\"item_table\"")
@Data
@NoArgsConstructor
@Getter
@Setter
public class Item {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="item_id")
	private int id;
	@Column(name="item_name")
	@NotEmpty(message = "Name must not be empty")
	private String name;
	@Column(name="price")
	@NotEmpty(message = "Price must not be empty")
	@Min(value = 0, message = "Price must be greater than or equal to 0")
	private double price;
	@JoinColumn(name="\"category_id\"")
	@OneToOne
	@NotEmpty(message = "Category must not be empty")
	private Category category;
	@JoinColumn(name="\"user_id\"")
	@OneToOne
	@NotEmpty(message = "User must not be empty")
	private User user;
	@Column(name="date")
	@NotEmpty(message = "Date must not be empty")
	private Date date;
	
}

