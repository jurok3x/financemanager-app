package com.financemanager.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {
    
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    CATEGORY_READ("category:read"),
    CATEGORY_WRITE("category:write"),
    EXPENSE_READ("expense:read"),
    EXPENSE_WRITE("expense:write"),
    INCOME_READ("income:read"),
    INCOME_WRITE("income:write");
    
    private final String permission;

}
