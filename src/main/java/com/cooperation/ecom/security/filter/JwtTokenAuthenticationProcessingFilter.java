package com.cooperation.ecom.security.filter;

import com.cooperation.ecom.exception.ApiError;
import com.cooperation.ecom.security.util.JwtAuthenticationToken;
import com.cooperation.ecom.security.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.file.Matcher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.List;


public class JwtTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
		
	private final List<String> pathsToSkip;
	
	private final JwtTokenUtil jwtTokenUtil;

	public JwtTokenAuthenticationProcessingFilter(RequestMatcher matcher
			, JwtTokenUtil jwtTokenUtil , List<String> pathsToSkip) {
		super(matcher);
		this.jwtTokenUtil = jwtTokenUtil;
		this.pathsToSkip = pathsToSkip;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)  {
		
		String header = request.getHeader("Authorization");

		boolean foundPathsToSkip = false;
		if(pathsToSkip!=null)
			for(String path : pathsToSkip) {
				if(Matcher.match(path, request.getServletPath(), false)) {
					foundPathsToSkip = true;
					break;
				}
			}

		if ( (header == null || !header.startsWith("Bearer ")) && !foundPathsToSkip  ) {
            throw new BadCredentialsException("Authorization header not found or incorrect credential type");
        }

		String token = "";
		if (header == null) {
			token = jwtTokenUtil.generateTokenGuest();
		}
		else if (header.startsWith("Bearer ")) {
			token = header.substring(7);
		}

        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
	}

	@Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();

		logger.info("Authentication failed");
		ObjectMapper mapper = new ObjectMapper();

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(mapper.writeValueAsString(new ApiError(request.getRequestURI(), "Authentication failed", HttpStatus.UNAUTHORIZED.value(), null)));
	}
}
