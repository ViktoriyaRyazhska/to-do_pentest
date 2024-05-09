package com.softserve.itacademy.config.security;

import com.softserve.itacademy.config.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        http.exceptionHandling(customizer -> customizer
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        );

        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(HttpMethod.POST, "/api/auth").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults());

        return http.build();
    }

    // @Bean
    //public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        http.exceptionHandling(customizer -> customizer
//                .authenticationEntryPoint(
//                        (request, response, authException) -> response.sendRedirect(request.getContextPath() + "/login"))
//        );
//        http.exceptionHandling(customizer -> customizer
//                .authenticationEntryPoint(
//                        (request, response, authException) -> handlerExceptionResolver.resolveException(request, response, null, authException)
//                )
//        );

//        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers(HttpMethod.GET, "/img/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/").permitAll()
//                .requestMatchers(HttpMethod.GET, "/login").permitAll()
//                .requestMatchers("/users/create").permitAll()
//                .anyRequest().authenticated()
//        );

//
//        http.addFilter(new WebAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()));
//        http.userDetailsService(domainUserDetailsService);

//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .httpBasic(withDefaults());

//        http.addFilter(new WebAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()));
//        http.userDetailsService(domainUserDetailsService);

        return http.build();
    }

}
