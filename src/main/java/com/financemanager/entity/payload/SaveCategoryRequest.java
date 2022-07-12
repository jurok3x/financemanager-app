package com.financemanager.entity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@AllArgsConstructor
public class SaveCategoryRequest {
    @NotBlank
    private String name;
}
