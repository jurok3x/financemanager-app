package com.financemanager.demo.site.entity.payload;

import com.financemanager.demo.site.dto.CategoryDTO;
import com.financemanager.demo.site.dto.UserDTO;

import lombok.Data;

import java.util.Date;

@Data
public class SaveItemRequest {
    
    private String name;
    private double price;
    private CategoryDTO category;
    private UserDTO user;
    private Date date;

}
