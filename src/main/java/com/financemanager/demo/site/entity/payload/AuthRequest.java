package com.financemanager.demo.site.entity.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AuthRequest {
	private String email;
    private String password;
}
