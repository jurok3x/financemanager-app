package com.financemanager.entity.payload;

import com.financemanager.dto.CategoryDTO;
import com.financemanager.dto.UserDTO;

import lombok.Data;

import java.util.Date;

@Data
public class SaveExpenseRequest {
    
    private String name;
    private double price;
    private CategoryDTO category;
    private UserDTO user;
    private Date date;

}
