package com.financemanager.demo.site.controller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AuthRequest {
	private String login;
    private String password;
}
