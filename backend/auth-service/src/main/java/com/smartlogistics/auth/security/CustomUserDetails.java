package com.smartlogistics.auth.security;

import com.smartlogistics.auth.domain.entity.UserCredential;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserCredential userCredential;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userCredential.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return userCredential.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return userCredential.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userCredential.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userCredential.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userCredential.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return userCredential.isEnabled();
    }
}
