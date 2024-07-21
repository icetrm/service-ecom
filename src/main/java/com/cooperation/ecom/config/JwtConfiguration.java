package com.cooperation.ecom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("jwt")
public class JwtConfiguration {

    private String issuer;
    private String signingKey;
    private Long tokenExpInSecs = 60L;
    private Long refreshTokenExpInSecs = 120L;

}
