package com.financemanager.demo.site.model;

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
public class ItemModel extends RepresentationModel<ItemModel> {
	
	private int id;
	private String name;
	private double price;
	private Date date;
	private CategoryModel category;
	private UserModel user;
}
