package com.cooperation.ecom.security.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;
import java.util.stream.Collectors;

public class SkipPathRequestMatcher implements RequestMatcher {
    private final OrRequestMatcher matchers;
    private final OrRequestMatcher processingMatcher;
    
	public SkipPathRequestMatcher(List<String> pathsToSkip, List<String> processingPath) {
        List<RequestMatcher> m = pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        matchers = new OrRequestMatcher(m);
        List<RequestMatcher> processing = processingPath.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        processingMatcher =  new OrRequestMatcher(processing);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (matchers.matches(request)) {
            return false;
        }
        
        return processingMatcher.matches(request);
    }
}
