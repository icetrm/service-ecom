package com.cooperation.ecom.security.util;

import com.cooperation.ecom.security.domain.UserSecurityContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	@Serial
	private static final long serialVersionUID = 8503082244222602958L;
	
	private String token;
	private UserSecurityContext userContext;

	public JwtAuthenticationToken(String token) {
		super(null);
		this.token = token;
		this.setAuthenticated(false);
	}

	public JwtAuthenticationToken(UserSecurityContext userContext, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.eraseCredentials();
		this.userContext = userContext;
		super.setAuthenticated(true);
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		if (authenticated) {
			throw new IllegalArgumentException(
					"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}
		super.setAuthenticated(false);
	}
	
	public String getUsername() {
		return userContext.getUsername();
	}

	@JsonIgnore
	@Override
	public Object getCredentials() {
		return token;
	}

	@JsonIgnore
	@Override
	public Object getPrincipal() {
		return userContext;
	}
	
	@JsonIgnore
	@Override
	public Object getDetails() {
		return super.getDetails();
	}
	
	@JsonIgnore
	@Override
	public String getName() {
		return super.getName();
	}

}
