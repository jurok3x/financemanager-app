package com.financemanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class IncomeModel extends RepresentationModel<IncomeModel>{
    private Long id;
    private String name;
    private Double amount;
    private Date date;
    private Integer userId;
}
