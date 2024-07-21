package com.cooperation.ecom.security.util;

import com.cooperation.ecom.config.JwtConfiguration;
import com.cooperation.ecom.security.domain.UserSecurityContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil implements Serializable {

	@Serial
	private static final long serialVersionUID = -4318259880742207327L;
	
	@Autowired
	private JwtConfiguration jwtConfiguration;
	
	static final String CLAIM_KEY_SCOPES = "scopes";
	static final String CLAIM_KEY_ROLE = "roles";
	
	static final String REFRESH_TOKEN_SCOPE = "refresh_token";
	
	static final String AUDIENCE_UNKNOWN = "unknown";
	
	
	private String generateAudience() {
        return AUDIENCE_UNKNOWN;
	}

	public String generateToken(UserSecurityContext userDetails) {
		Claims claims = Jwts.claims().subject(userDetails.getId()).add(CLAIM_KEY_ROLE, userDetails.getAuthorities().stream().map(s -> s.getAuthority()).collect(Collectors.toList())).build();
		
		return doGenerateToken(claims, generateAudience());
	}

	private String doGenerateToken(Map<String, Object> claims, String audience) {
		final Date createdDate = roundUpDate(new Date());
		final Date expirationDate = calculateExpirationDate(createdDate, jwtConfiguration.getTokenExpInSecs());

		return Jwts.builder()
				.header().type("JWT").and()
				.claims(claims)
				.issuer(jwtConfiguration.getIssuer())
				.issuedAt(createdDate)
				.audience().add(audience)
				.and()
				.expiration(expirationDate)
				.signWith(getSignInKey(), Jwts.SIG.HS256)
				.compact();
	}
	
	public String generateTokenGuest() {
		Claims claims = Jwts.claims().subject("guest").add(CLAIM_KEY_ROLE, List.of("guest")).build();
		return doGenerateToken(claims, generateAudience());
	}

	public String generateRefreshToken(UserSecurityContext userDetails) {
		Claims claims = Jwts.claims().subject(userDetails.getUsername()).add(CLAIM_KEY_SCOPES, List.of(REFRESH_TOKEN_SCOPE)).build();

		return doGenerateRefreshToken(claims, generateAudience());
	}
	
	private String doGenerateRefreshToken(Map<String, Object> claims, String audience) {
		final Date createdDate = roundUpDate(new Date());
		final Date expirationDate = calculateExpirationDate(createdDate, jwtConfiguration.getRefreshTokenExpInSecs());

		return Jwts.builder()
				.header().type("JWT").and()
				.claims(claims)
				.id(UUID.randomUUID().toString())
				.issuer(jwtConfiguration.getIssuer())
				.issuedAt(createdDate)
				.audience().add(audience)
				.and()
				.expiration(expirationDate)
				.signWith(getSignInKey(), Jwts.SIG.HS256)
				.compact();
	}
	
	private Date calculateExpirationDate(Date createdDate, Long expirationTime) {
		return new Date(createdDate.getTime() + expirationTime * 1000);
	}
	
	private Date roundUpDate(Date createdDate) {
		long time = ((createdDate.getTime() + 999) / 1000) * 1000;
		return new Date(time);
	}
	
	public boolean validateToken(String token , Date lastLogin) {
		return (isValidIssuer(token) && !isTokenExpired(token)
				&& !isCreatedBeforeLastLoginReset(getIssuedAtDateFromToken(token), lastLogin));
	}
	
	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public String getIssuerFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuer);
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims getAllClaimsFromToken(String token) {
		try {
			return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
		} catch (ExpiredJwtException e) {
			throw new BadCredentialsException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getScopesFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		List<String> scopes = claims.get(CLAIM_KEY_SCOPES, List.class);
		return scopes;
	}
	
	private Boolean isValidIssuer(String token) {
		return jwtConfiguration.getIssuer().equals(getIssuerFromToken(token));
	}
	
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	public Boolean canTokenBeRefreshed(String token , Date lastLogin) {
		final Date created = getIssuedAtDateFromToken(token);
		return !isCreatedBeforeLastLoginReset(created, lastLogin)
				&& hasRefreshTokenScope(token)
				&& !isTokenExpired(token);
	}
	
	private Boolean isCreatedBeforeLastLoginReset(Date created, Date lastLogin) {
		return (lastLogin != null && created.before(lastLogin));
	}
	
	private Boolean hasRefreshTokenScope(String token) {
		List<String> scopes = getScopesFromToken(token);
		if (!scopes.contains(REFRESH_TOKEN_SCOPE)) {
			throw new BadCredentialsException("");
		}
		return true;
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtConfiguration.getSigningKey());
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
