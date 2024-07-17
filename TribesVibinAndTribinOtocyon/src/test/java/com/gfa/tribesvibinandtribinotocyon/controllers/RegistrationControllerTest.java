package com.gfa.tribesvibinandtribinotocyon.controllers;

import com.gfa.tribesvibinandtribinotocyon.dtos.RegisterRequest;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesInternalServerErrorException;
import com.gfa.tribesvibinandtribinotocyon.services.UserService;
import com.gfa.tribesvibinandtribinotocyon.services.emailValidagtion.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RegistrationControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @InjectMocks
    private RegistrationController registrationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_ValidUser_Returns200() throws IOException, TribesInternalServerErrorException {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        registerRequest.setEmail("testemail@test.test");
        when(userService.doesUserExist(registerRequest.getUsername())).thenReturn(false);

        ResponseEntity<?> responseEntity = registrationController.register(registerRequest);

        assertEquals(200, responseEntity.getStatusCodeValue());
        verify(userService, times(1)).createUser(registerRequest);
    }

    @Test
    void register_UserAlreadyExists_Returns409() throws IOException, TribesInternalServerErrorException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        registerRequest.setEmail("testemail@test.test");
        when(userService.doesUserExist(registerRequest.getUsername())).thenReturn(true);

        ResponseEntity<?> responseEntity = registrationController.register(registerRequest);

        assertEquals(409, responseEntity.getStatusCodeValue());
        verify(userService, never()).createUser(registerRequest);
    }

    @Test
    void verifyUser_ValidToken_ReturnsSuccessMessage() {
        String username = "testUser";
        String token = "testToken";
        when(confirmationTokenService.verifyUser(username, token)).thenReturn("User verified successfully.");

        String result = registrationController.verifyUser(username, token);

        assertEquals("User verified successfully.", result);
    }

    @Test
    void verifyUser_InvalidToken_ReturnsErrorMessage() {
        String username = "testUser";
        String token = "invalidToken";
        when(confirmationTokenService.verifyUser(username, token)).thenReturn("Invalid token.");

        String result = registrationController.verifyUser(username, token);

        assertEquals("Invalid token.", result);
    }
}
