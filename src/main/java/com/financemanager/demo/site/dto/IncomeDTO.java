package com.financemanager.demo.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDTO {
    private Long id;
    private String name;
    private double amount;
    private Date date;
    private UserDTO userDTO;
}
