package com.gfa.tribesvibinandtribinotocyon.services.emailValidagtion;


import com.gfa.tribesvibinandtribinotocyon.models.UserEntity;
import com.gfa.tribesvibinandtribinotocyon.repositories.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserEntityRepository userEntityRepository;
    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }
    
    public String verifyUser(String username, String token){
        Optional<ConfirmationToken> tokenOpt = confirmationTokenRepository.findByToken(token);
        Optional<UserEntity> userOpt = userEntityRepository.findByUsername(username);
        if (userOpt.isEmpty()){
            return "Thou shall not access my website, stinky hacker";
        }
        if (token == null || !userOpt.get().getConfirmationToken().getToken().equals(token)){
            return "Invalid confirmation Token";
        }else if (tokenOpt.get().getExpiresAt().isBefore(LocalDateTime.now())){
            return "Token expired";
        }
        userOpt.get().setEnabled(true);
        tokenOpt.get().setConfirmed(true);
        confirmationTokenRepository.save(tokenOpt.get());
        userEntityRepository.save(userOpt.get());
        return "Success,welcome to the family";
    }

    public ConfirmationToken createConfirmationToken(){
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );
        return confirmationTokenRepository.save(confirmationToken);
    }
}
