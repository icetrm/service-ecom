package com.cooperation.ecom.security.controller;

import com.cooperation.ecom.security.domain.InfoResponse;
import com.cooperation.ecom.security.domain.JwtAuthenticationRequest;
import com.cooperation.ecom.security.domain.JwtAuthenticationResponse;
import com.cooperation.ecom.security.domain.UserSecurityContext;
import com.cooperation.ecom.security.service.JwtUserDetailsService;
import com.cooperation.ecom.security.util.JwtAuthenticationToken;
import com.cooperation.ecom.security.util.JwtTokenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/auth/jwt")
public class SecurityController {

    private static final Logger logger = LogManager.getLogger(SecurityController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest request) {
        logger.info("user request create authentication token {}", request.getUsername());

        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserSecurityContext user = (UserSecurityContext) authentication.getPrincipal();

        final String token = jwtTokenUtil.generateToken(user);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        logger.info("user create authentication token success {}", request.getUsername());

        return ResponseEntity.ok(new JwtAuthenticationResponse(token, refreshToken));
    }

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> expireAuthenticationToken(@RequestHeader(value = "Authorization") String header) {
        logger.info("user request expire authentication token");

        String token = header.substring(7);


        final JwtAuthenticationToken authenticatedToken = (JwtAuthenticationToken) authenticationManager
                .authenticate(new JwtAuthenticationToken(token));

        UserDetails userDetails = jwtUserDetailsService.logout(((UserSecurityContext) authenticatedToken.getPrincipal()).getUsername());

        logger.info("user has expire authentication token {} ", userDetails.getUsername());

        return ResponseEntity.ok(new JwtAuthenticationResponse("", ""));
    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshAuthenticationToken(@RequestHeader(value = "Authorization") String oldRefreshToken) {
        logger.info("user request refresh authentication token");

        String username = jwtTokenUtil.getUsernameFromToken(oldRefreshToken);
        UserSecurityContext user = jwtUserDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(oldRefreshToken, user.getLastLogin())) {
            jwtUserDetailsService.logout(username);

            final String token = jwtTokenUtil.generateToken(user);
            final String refreshToken = jwtTokenUtil.generateRefreshToken(user);

            logger.info("user refresh authentication token success {}", username);
            return ResponseEntity.ok(new JwtAuthenticationResponse(token, refreshToken));
        }

        throw new BadCredentialsException("Token Invalid");
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthenticationTokenInfo(@RequestHeader(value = "Authorization") String header) {

        String token = header.substring(7);

        final JwtAuthenticationToken authenticatedToken = (JwtAuthenticationToken) authenticationManager
                .authenticate(new JwtAuthenticationToken(token));

        UserSecurityContext userDetails = jwtUserDetailsService.loadUserByUsername(((UserSecurityContext) authenticatedToken.getPrincipal()).getUsername());

        logger.info("user get info {} ", userDetails.getId());
        if (jwtTokenUtil.validateToken(token, userDetails.getLastLogin())) {
            return ResponseEntity.ok(new InfoResponse(authenticatedToken.isAuthenticated(), authenticatedToken.getAuthorities(), authenticatedToken.getUsername()));
        }

        throw new BadCredentialsException("Token Invalid");
    }

}
