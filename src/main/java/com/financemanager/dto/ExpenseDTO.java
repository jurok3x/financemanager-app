package com.financemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {   
    private Long id;
    private String name;
    private double price;
    private CategoryDTO categoryDTO;
    private UserDTO userDTO;
    private Date date;
}
