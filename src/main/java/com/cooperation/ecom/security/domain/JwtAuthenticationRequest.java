package com.cooperation.ecom.security.domain;

import lombok.Data;

@Data
public class JwtAuthenticationRequest {

	private String username;
	private String password;

}
