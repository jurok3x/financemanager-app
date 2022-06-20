package com.financemanager.demo.site.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

import java.util.Date;

@Entity
@Table(name = "\"income_table\"")
@Data
@NoArgsConstructor
@Getter
@Setter
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="income_id")
    private Long id;
    @Column(name="income_name")
    @NotBlank(message = "Name must not be empty")
    private String name;
    @Column(name="amount")
    @NotNull(message = "Amount must not be null")
    @Min(value = 0, message = "Amount should be be greater then zero")
    private double amount;
    @Column(name="date")
    @Valid
    @NotNull(message = "Date must not be null")
    private Date date;
    @JoinColumn(name="\"user_id\"")
    @OneToOne
    private User user;
}
