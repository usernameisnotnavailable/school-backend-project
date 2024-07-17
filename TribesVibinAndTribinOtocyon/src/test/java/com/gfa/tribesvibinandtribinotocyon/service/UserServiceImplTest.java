package com.gfa.tribesvibinandtribinotocyon.service;

import com.gfa.tribesvibinandtribinotocyon.dtos.RegisterRequest;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesInternalServerErrorException;
import com.gfa.tribesvibinandtribinotocyon.models.UserEntity;
import com.gfa.tribesvibinandtribinotocyon.models.roles.RoleUser;
import com.gfa.tribesvibinandtribinotocyon.repositories.RoleRepository;
import com.gfa.tribesvibinandtribinotocyon.repositories.UserEntityRepository;
import com.gfa.tribesvibinandtribinotocyon.services.UserServiceImpl;
import com.gfa.tribesvibinandtribinotocyon.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestPropertySource(locations = "classpath:application-test.properties")
class UserServiceImplTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doesUserExist_ExistingUser_ReturnsTrue() {
        String username = "existingUser";
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(new UserEntity()));

        assertTrue(userService.doesUserExist(username));
    }

    @Test
    void doesUserExist_NonExistingUser_ReturnsFalse() {
        String username = "nonExistingUser";
        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertFalse(userService.doesUserExist(username));
    }



    @Test
    void createUser_UserAlreadyExists_ReturnsEmptyOptional() throws TribesInternalServerErrorException, IOException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newUser");
        registerRequest.setPassword("password");
        registerRequest.setEmail("email@example.com");
        when(userEntityRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(new UserEntity()));

        Optional<UserEntity> createdUser = userService.createUser(registerRequest);

        assertTrue(createdUser.isEmpty());
        verify(userEntityRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void testGetUserByUsername_ExistingUser_ReturnsUserEntity() {
        String existingUsername = "existingUser";
        UserEntity existingUser = new UserEntity();
        when(userEntityRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));

        Optional<UserEntity> result = userService.getUserByUsername(existingUsername);

        assertTrue(result.isPresent());
        assertEquals(existingUser, result.get());
    }

    @Test
    public void testGetUserByUsername_NonExistingUser_ReturnsEmptyOptional() {
        String nonExistingUsername = "nonExistingUser";
        when(userEntityRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.getUserByUsername(nonExistingUsername);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetUserByEmail_ExistingUser_ReturnsUserEntity() {
        String existingEmail = "existingUser@example.com";
        UserEntity existingUser = new UserEntity();
        when(userEntityRepository.findByEmail(existingEmail)).thenReturn(Optional.of(existingUser));

        Optional<UserEntity> result = userService.getUserByEmail(existingEmail);

        assertTrue(result.isPresent());
        assertEquals(existingUser, result.get());
    }

    @Test
    public void testGetUserByEmail_NonExistingUser_ReturnsEmptyOptional() {
        String nonExistingEmail = "nonExistingUser@example.com";
        when(userEntityRepository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.getUserByEmail(nonExistingEmail);

        assertTrue(result.isEmpty());
    }
}
