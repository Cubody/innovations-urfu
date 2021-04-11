package com.greenorine.innovationsurfuapi.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.greenorine.innovationsurfuapi.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class JwtUser implements UserDetails {

    private final Long id;
    private final String fullName;
    private final String email;
    private final boolean enabled;

    public JwtUser(Long id, String fullName, String email, boolean enabled) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(Role.values()).map(role ->
                new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return null;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return fullName;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @JsonInclude
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
