package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.RestAuthRequestDto;
import com.softserve.itacademy.dto.RestAuthResponseDto;
import com.softserve.itacademy.config.security.WebAuthenticationProvider;
import com.softserve.itacademy.config.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequiredArgsConstructor
public class RestLoginController {

    private final WebAuthenticationProvider webAuthenticationProvider;
    private final JwtService jwtService;

    @PostMapping("/api/auth")
    public ResponseEntity<RestAuthResponseDto> login(@RequestBody RestAuthRequestDto requestDto) {
        Authentication authenticated = webAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
        );

        if (authenticated == null) {
            return ResponseEntity.status(UNAUTHORIZED).build();
        }

        String token = jwtService.generateToken(authenticated.getName());

        RestAuthResponseDto dto = RestAuthResponseDto.builder().token(token).build();
        return ResponseEntity.ok(dto);
    }
}
