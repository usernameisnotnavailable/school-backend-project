package com.gfa.tribesvibinandtribinotocyon.controllers;

import com.gfa.tribesvibinandtribinotocyon.dtos.ErrorMessageDto;
import com.gfa.tribesvibinandtribinotocyon.dtos.RegisterRequest;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesInternalServerErrorException;
import com.gfa.tribesvibinandtribinotocyon.repositories.UserEntityRepository;
import com.gfa.tribesvibinandtribinotocyon.services.UserService;
import com.gfa.tribesvibinandtribinotocyon.services.emailValidagtion.ConfirmationTokenRepository;
import com.gfa.tribesvibinandtribinotocyon.services.emailValidagtion.ConfirmationTokenService;
import com.gfa.tribesvibinandtribinotocyon.services.emailValidagtion.EmailStructure;
import com.gfa.tribesvibinandtribinotocyon.services.emailValidagtion.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class RegistrationController{

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) throws TribesInternalServerErrorException, IOException {

        if (registerRequest.getUsername() == null){
            return ResponseEntity.status(400).body(new ErrorMessageDto("Missing parameter: username!", LocalDateTime.now()));
        } else if (registerRequest.getPassword() == null) {
            return ResponseEntity.status(400).body(new ErrorMessageDto("Missing parameter: password!", LocalDateTime.now()));
        } else if (userService.doesUserExist(registerRequest.getUsername())) {
            return ResponseEntity.status(409).body(new ErrorMessageDto("Username already taken, please choose another one", LocalDateTime.now()));
        } else{
            return ResponseEntity.status(200).body(userService.createUser(registerRequest));
        }
    }

    @GetMapping("/verify/{username}/{token}")
    public String verifyUser(@PathVariable String username,
                             @PathVariable String token){
        return confirmationTokenService.verifyUser(username, token);
    }
}