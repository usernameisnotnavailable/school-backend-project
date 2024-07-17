package com.gfa.tribesvibinandtribinotocyon.controllers;

import com.gfa.tribesvibinandtribinotocyon.dtos.AuthenticationRequest;
import com.gfa.tribesvibinandtribinotocyon.dtos.ErrorMessageDto;
import com.gfa.tribesvibinandtribinotocyon.dtos.RegisterRequest;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesInternalServerErrorException;
import com.gfa.tribesvibinandtribinotocyon.services.AuthenticationService;
import com.gfa.tribesvibinandtribinotocyon.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserDetailsService userDetailsService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody(required = false) AuthenticationRequest request){
        if(request == null) {
            return ResponseEntity.status(400).body(new ErrorMessageDto("Body of request should be specified with email and password.", LocalDateTime.now()));
        }
        if (request.getPassword() == null && request.getEmail() == null){
            return ResponseEntity.status(400).body(new ErrorMessageDto("Please enter your email and password.", LocalDateTime.now()));
        } else if (request.getPassword() == null ){
            return ResponseEntity.status(400).body(new ErrorMessageDto("Please enter your password.", LocalDateTime.now()));
        } else if (request.getEmail() == null) {
            return ResponseEntity.status(400).body(new ErrorMessageDto("Please enter your email.", LocalDateTime.now()));
        }
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}