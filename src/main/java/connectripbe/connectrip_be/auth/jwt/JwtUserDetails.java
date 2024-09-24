package connectripbe.connectrip_be.auth.jwt;

import connectripbe.connectrip_be.auth.jwt.dto.CustomUserDto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record JwtUserDetails(CustomUserDto customUserDto) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(customUserDto.getRoleType()));
    }

    public Long getId() {
        return customUserDto.getId();
    }

    @Override
    public String getPassword() {
        return customUserDto.getPassword();
    }

    @Override
    public String getUsername() {
        return customUserDto.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
