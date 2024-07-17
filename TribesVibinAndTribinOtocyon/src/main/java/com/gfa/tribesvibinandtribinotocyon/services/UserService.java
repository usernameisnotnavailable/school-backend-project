package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.dtos.RegisterRequest;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesInternalServerErrorException;
import com.gfa.tribesvibinandtribinotocyon.models.UserEntity;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    boolean doesUserExist(String username);

    Optional<UserEntity> createUser(RegisterRequest registerRequest) throws TribesInternalServerErrorException, IOException;

    Optional<UserEntity> getUserByUsername(String username);

    Optional<UserEntity> getUserByEmail(String email);
}