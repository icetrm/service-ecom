package com.cooperation.ecom.security.domain;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;

public class RoleGrantedAuthority implements GrantedAuthority{

	@Serial
	private static final long serialVersionUID = -506769701454450098L;
	
	private final String role;
	
	public RoleGrantedAuthority(String role) {
		this.role = role;
	}

	@Override
	public String getAuthority() {
		return role;
	}
}
