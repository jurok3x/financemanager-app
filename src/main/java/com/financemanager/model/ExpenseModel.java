package com.financemanager.model;

import java.util.Date;

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
public class ExpenseModel extends RepresentationModel<ExpenseModel> {
	
	private Long id;
	private String name;
	private double price;
	private Date date;
	private CategoryModel category;
	private UserModel user;
}
