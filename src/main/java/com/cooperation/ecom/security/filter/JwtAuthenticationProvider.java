package com.cooperation.ecom.security.filter;

import com.cooperation.ecom.entity.User;
import com.cooperation.ecom.repository.UserRepository;
import com.cooperation.ecom.security.domain.RoleGrantedAuthority;
import com.cooperation.ecom.security.domain.UserSecurityContext;
import com.cooperation.ecom.security.util.JwtAuthenticationToken;
import com.cooperation.ecom.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserRepository userRepository;

	public JwtAuthenticationProvider() {
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String token = (String) authentication.getCredentials();
		String username = jwtTokenUtil.getUsernameFromToken(token);

		User user = userRepository.findByUsername(username);
		if (user == null || !jwtTokenUtil.validateToken(token, user.getLastLogin())) {
			throw new BadCredentialsException("Invalid Token");
		}

		List<GrantedAuthority> roles = Arrays.asList(new RoleGrantedAuthority(user.getRole().getTitle()));
		UserSecurityContext context = new UserSecurityContext(username, username, null, new Date(), roles);
		return new JwtAuthenticationToken(context, roles);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
