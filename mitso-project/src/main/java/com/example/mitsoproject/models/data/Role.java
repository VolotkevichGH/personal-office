package com.example.mitsoproject.models.data;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_STUDENT , ROLE_CURATOR , ROLE_ADMIN, ROLE_TEACHER, ROLE_DECAN;

    @Override
    public String getAuthority() {
        return name();
    }
}