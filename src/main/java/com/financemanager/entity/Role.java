package com.financemanager.entity;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.financemanager.entity.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER(Arrays.asList(USER_READ, USER_WRITE, CATEGORY_READ, CATEGORY_WRITE,
            INCOME_READ, INCOME_WRITE, EXPENSE_READ, EXPENSE_WRITE).stream().collect(Collectors.toSet())),
    ADMIN(Arrays.asList(USER_READ, USER_WRITE, CATEGORY_READ, CATEGORY_WRITE,
            INCOME_READ, INCOME_WRITE, EXPENSE_READ, EXPENSE_WRITE).stream().collect(Collectors.toSet()));
    
    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> userPermissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());
        userPermissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return userPermissions;
    }
    
    private final Set<Permission> permissions;
}
