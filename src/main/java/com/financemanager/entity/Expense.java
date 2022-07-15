package com.financemanager.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "\"expenses\"")
@Data
@NoArgsConstructor
@Getter
@Setter
public class Expense {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	@Column(name="name")
	@NotBlank(message = "Name must not be empty")
	private String name;
	@Column(name="price")
	@NotNull(message = "Price must not be null")
	@Min(value = 0, message = "Price should be be greater then zero")
	private double price;
	@JoinColumn(name="\"category_id\"")
	@OneToOne
	@Valid
	@NotNull(message = "Category must not be null")
	private Category category;
	@JoinColumn(name="\"user_id\"")
	@OneToOne
	private User user;
	@Column(name="date")
	@Valid
	@NotNull(message = "Date must not be null")
	private Date date;
}

