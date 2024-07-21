package com.cooperation.ecom.security.service;

import com.cooperation.ecom.entity.User;
import com.cooperation.ecom.repository.UserRepository;
import com.cooperation.ecom.security.domain.RoleGrantedAuthority;
import com.cooperation.ecom.security.domain.UserSecurityContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private static final Logger logger = LogManager.getLogger(JwtUserDetailsService.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserSecurityContext loadUserByUsername(String username) {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new BadCredentialsException("Authentication Failed. Username or Password not valid");
		}

		return new UserSecurityContext(username, username, user.getPassword(), user.getLastLogin(), List.of(new RoleGrantedAuthority(user.getRole().getTitle())));
	}

	public void updateUserLogin(UserSecurityContext user , boolean isLoginPass) {
		if(isLoginPass) {
			User exportUser = userRepository.findByUsername(user.getId());
			logger.info("user update login date {} ", user.getId());
			exportUser.setLastLogin(new Date());
			userRepository.save(exportUser);
		}
	}

	public UserSecurityContext logout(String username) {
		User exportUser = userRepository.findByUsername(username);
		logger.info("user update logout date {} ", username);
		exportUser.setLastLogin(new Date());
		userRepository.save(exportUser);
		return new UserSecurityContext(username, username, exportUser.getPassword(), exportUser.getLastLogin(), List.of(new RoleGrantedAuthority(exportUser.getRole().getTitle())));
	}
}
