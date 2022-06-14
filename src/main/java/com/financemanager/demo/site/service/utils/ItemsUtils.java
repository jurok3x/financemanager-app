package com.financemanager.demo.site.service.utils;

import com.financemanager.demo.site.entity.CustomUserDetails;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ItemsUtils {
    
    private static final String MONTH = "%%-%02d%%";
    private static final String EMPTY = "%-%";
    private static final String YEAR = "%%%d-%%";
    private static final String YEAR_MONTH = "%%%2$d-%1$02d%%";
    
    private final static CustomUserDetails CONTEXT_USER = (CustomUserDetails) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
    
    public static String formatDateString(Optional<Integer> year, Optional<Integer> month) {
        if (year.isPresent()) {
            if (month.isPresent()) {
                return String.format(YEAR_MONTH, month.get(), year.get());
            }
            String.format(YEAR, year.get());
        }
        if (!month.isPresent()) {
            return EMPTY;
        }
        return String.format(MONTH, month.get());
    }
    
    public static CustomUserDetails getContextUser() {
        return CONTEXT_USER;
    }

}
