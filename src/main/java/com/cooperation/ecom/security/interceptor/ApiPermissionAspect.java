package com.cooperation.ecom.security.interceptor;

import com.cooperation.ecom.exception.ResourceNotFoundException;
import com.cooperation.ecom.security.domain.RoleGrantedAuthority;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class ApiPermissionAspect {
	
	@Before("@annotation(allowRoles)")
	public void verifyRoles(JoinPoint joinPoint, AllowRoles allowRoles) {

		List<RoleGrantedAuthority> authorityRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.map(authority -> (RoleGrantedAuthority)authority).toList();
		
		authorityRoles.stream().filter(authorityRole -> Arrays.asList(allowRoles.roles()).contains(authorityRole.getAuthority()))
		.findFirst().orElseThrow(() -> new ResourceNotFoundException(SecurityContextHolder.getContext().getAuthentication().getName() + " is Permission denied"));
	}
}