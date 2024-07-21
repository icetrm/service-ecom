package com.cooperation.ecom.security.filter;

import com.cooperation.ecom.security.domain.UserSecurityContext;
import com.cooperation.ecom.security.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

	private final JwtUserDetailsService userDetailsService;

	@Autowired
	public UsernamePasswordAuthenticationProvider(final JwtUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.notNull(authentication, "No authentication data provided");

		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		UserSecurityContext user = (UserSecurityContext)  userDetailsService.loadUserByUsername(username);
		boolean isValid = encoder.matches(password, user.getPassword());

		if (!isValid) {
			userDetailsService.updateUserLogin(user , false);
			throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
		}

		userDetailsService.updateUserLogin(user , true);
		return new UsernamePasswordAuthenticationToken(user, null, null);
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
