package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.dtos.RegisterRequest;
import com.gfa.tribesvibinandtribinotocyon.models.roles.Role;
import com.gfa.tribesvibinandtribinotocyon.models.UserEntity;
import com.gfa.tribesvibinandtribinotocyon.repositories.RoleRepository;
import com.gfa.tribesvibinandtribinotocyon.repositories.UserEntityRepository;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesInternalServerErrorException;
import com.gfa.tribesvibinandtribinotocyon.services.emailValidagtion.ConfirmationToken;
import com.gfa.tribesvibinandtribinotocyon.services.emailValidagtion.ConfirmationTokenService;
import com.gfa.tribesvibinandtribinotocyon.services.emailValidagtion.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserEntityRepository userEntityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final MailSenderService mailSenderService;

    @Override
    public boolean doesUserExist(String username) {
        return userEntityRepository.findByUsername(username).isPresent();
    }

    @Override
    public Optional<UserEntity> createUser(RegisterRequest registerRequest) throws TribesInternalServerErrorException, IOException {
        if (registerRequest == null ||
                registerRequest.getPassword() == null ||
                registerRequest.getUsername() == null ||
                registerRequest.getEmail() == null ||
                doesUserExist(registerRequest.getUsername())) {
            return Optional.empty();
        }

        UserEntity user = UserEntity
                .builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .build();

        //create and set new confirmation token for email verification
        user.setConfirmationToken(confirmationTokenService.createConfirmationToken());
        //send actual confirmation email
        mailSenderService.sendVerificationEmail(user.getEmail(),mailSenderService.linkGenerator(user.getUsername(),user.getConfirmationToken().getToken()),user.getUsername());
        Optional<Role> roleOptional = roleRepository.findByNameIgnoreCase("user");
        if (roleOptional.isPresent()) {
            user.setRole(roleOptional.get());

            return Optional.of(userEntityRepository.save(user));
        } else {
            throw new TribesInternalServerErrorException("User role not found");
        }
    }

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        return userEntityRepository.findByUsername(username);
    }

    @Override
    public Optional<UserEntity> getUserByEmail(String email) {
        return userEntityRepository.findByEmail(email);
    }
}