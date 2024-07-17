package com.gfa.tribesvibinandtribinotocyon.controllers;

import com.gfa.tribesvibinandtribinotocyon.dtos.AuthenticationRequest;
import com.gfa.tribesvibinandtribinotocyon.dtos.AuthenticationResponse;
import com.gfa.tribesvibinandtribinotocyon.dtos.ErrorMessageDto;
import com.gfa.tribesvibinandtribinotocyon.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AuthenticationControllerTest {

    @Mock
    AuthenticationService authenticationServiceMock;

    @InjectMocks
    AuthenticationController authenticationController;

    @Test
    public void authenticate_ValidData_Passes() throws Exception {
        String username = "test";
        String email = "test@test.com";
        String password = "123";

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, username, password);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken("test_token");

        when(authenticationServiceMock.authenticate(authenticationRequest)).thenReturn(authenticationResponse);

        ResponseEntity<?> responseEntity = authenticationController.authenticate(authenticationRequest);

        assertEquals(200, responseEntity.getStatusCode().value());
        verify(authenticationServiceMock, times(1)).authenticate(any());
    }

    @Test
    void authenticate_NoPassword_ReturnsBadRequest() {
        String username = "test";
        String email = "test@test.com";
        String password = null;

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, username, password);

        ErrorMessageDto errorMessageDto = new ErrorMessageDto();
        errorMessageDto.setMessage("Please enter your password");
        errorMessageDto.setTimestamp(LocalDateTime.now());

        ResponseEntity<?> responseEntity = authenticationController.authenticate(authenticationRequest);

        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals("Please enter your password.", ((ErrorMessageDto) responseEntity.getBody()).getMessage());
        assertNotNull(responseEntity.getBody());
        verify(authenticationServiceMock, times(0)).authenticate(any());
    }

}