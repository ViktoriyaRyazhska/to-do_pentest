package com.softserve.itacademy.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {
        try {
            final String authHeader = request.getHeader("Authorization");
            if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUserName(jwt);
            if (StringUtils.hasText(userEmail)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail, null, null);

//            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userEmail)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}