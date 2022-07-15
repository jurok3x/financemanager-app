package com.financemanager.demo.site.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String email;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}
	
	public static CustomUserDetails fromUserToCustomUserDetails(User user) {
		CustomUserDetails userDetails = new CustomUserDetails();
		userDetails.id = user.getId();
		userDetails.email = user.getEmail();
		userDetails.password = user.getPassword();
		userDetails.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName()));
		return userDetails;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}
	
	public Integer getId() {
        return this.id;
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