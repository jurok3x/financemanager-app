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
import javax.validation.GroupSequence;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.financemanager.demo.site.entity.validation.AdvancedValidation;
import com.financemanager.demo.site.entity.validation.BasicValidation;

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
@GroupSequence(value = {BasicValidation.class, AdvancedValidation.class})
public class Item {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="item_id")
	private int id;
	@Column(name="item_name")
	@NotBlank(message = "Name must not be empty", groups = BasicValidation.class)
	private String name;
	@Column(name="price")
	@NotNull(message = "Price must not be null", groups = BasicValidation.class)
	@Min(value = 0, message = "Price be greater then zero")
	private double price;
	@JoinColumn(name="\"category_id\"")
	@OneToOne
	@Valid
	@NotNull(message = "Category must not be null", groups = BasicValidation.class)
	private Category category;
	@JoinColumn(name="\"user_id\"")
	@OneToOne
	@Valid
	@NotNull(message = "User must not be null", groups = AdvancedValidation.class)
	private User user;
	@Column(name="date")
	@Valid
	@NotNull(message = "Date must not be null", groups = BasicValidation.class)
	private Date date;
}

