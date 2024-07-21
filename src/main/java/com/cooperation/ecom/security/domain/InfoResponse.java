package com.cooperation.ecom.security.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
@Data
public class InfoResponse {
    private boolean authenticated;
    private Collection<GrantedAuthority> authorities;
    private String username;

    public InfoResponse() {
    }

    public InfoResponse(boolean authenticated, Collection<GrantedAuthority> authorities, String username) {
        this.authenticated = authenticated;
        this.authorities = authorities;
        this.username = username;
    }

}
