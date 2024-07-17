package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.configs.JwtService;
import com.gfa.tribesvibinandtribinotocyon.dtos.AuthenticationRequest;
import com.gfa.tribesvibinandtribinotocyon.dtos.AuthenticationResponse;
import com.gfa.tribesvibinandtribinotocyon.models.roles.Role;
import com.gfa.tribesvibinandtribinotocyon.models.roles.RoleUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    JwtService jwtServiceMock;

    @Mock
    AuthenticationManager authenticationManagerMock;

    @InjectMocks
    AuthenticationService authenticationService;

    @Test
    void authenticate_IfRequestValid_ReturnsResponseOK() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("test");
        authenticationRequest.setEmail("test@test.com");
        authenticationRequest.setPassword("123");

        Role role = new RoleUser();
        role.setName("USER");
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getName());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword(), List.of(simpleGrantedAuthority));

        when(authenticationManagerMock.authenticate(any())).thenReturn(authenticationToken);
        when(jwtServiceMock.generateToken(authenticationToken)).thenReturn("generatedToken");

        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);

        verify(authenticationManagerMock).authenticate(any());
        verify(jwtServiceMock, times(1)).generateToken(authenticationToken);

        assertEquals("generatedToken", authenticationResponse.getToken());
    }

    @Test
    void authenticate_IfInvalidPassword_ThrowsException() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("test");
        authenticationRequest.setEmail("test@test.com");
        authenticationRequest.setPassword("invalidPWD");

        when(authenticationManagerMock.authenticate(any())).thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(authenticationRequest));

        verify(authenticationManagerMock).authenticate(any());
    }

}