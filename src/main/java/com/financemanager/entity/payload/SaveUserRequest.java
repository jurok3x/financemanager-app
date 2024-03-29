package com.financemanager.entity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserRequest {
    
    @NotBlank(message = "Firstname must not be empty.")
    private String name;

    @NotBlank(message = "Password must not be empty.")
    private String password;

    @NotBlank(message = "Email must not be empty.")
    @Email(message = "Email must be a valid email address.")
    private String email;

}
