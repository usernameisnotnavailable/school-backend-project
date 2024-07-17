package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.dtos.RegisterRequest;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesInternalServerErrorException;
import com.gfa.tribesvibinandtribinotocyon.models.Kingdom;
import com.gfa.tribesvibinandtribinotocyon.models.UserEntity;
import com.gfa.tribesvibinandtribinotocyon.repositories.KingdomRepository;
import com.gfa.tribesvibinandtribinotocyon.repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class DatabaseFiller {
        private final UserServiceImpl userService;
        private final KingdomService kingdomService;
        private final KingdomRepository kingdomRepository;
    public void fillDatabaseWithFew() throws TribesInternalServerErrorException, IOException {
        Random random = new Random();
        for (int i=0;i<5;i++){

            RegisterRequest request = new RegisterRequest();
            request.setUsername("testUser"+ i);
            request.setPassword("password"+ random.nextInt(100));
            request.setEmail("real.email"+random.nextInt(10000)+"@tribes.com");
            userService.createUser(request);

            kingdomService.createKingdom("testKingdom"+i, random.nextInt(100), random.nextInt(100),userService.getUserByUsername("testUser"+i).get());
        }


    }

    public void fillDatabaseWithALot() throws TribesInternalServerErrorException, IOException {
        Random random = new Random();
        for (int i=0;i<50;i++){

            RegisterRequest request = new RegisterRequest();
            request.setUsername("testUser"+ i);
            request.setPassword("password"+ random.nextInt(200));
            request.setEmail("real.email"+random.nextInt(10000)+"@tribes.com");
            userService.createUser(request);

            kingdomService.createKingdom("testKingdom"+i, random.nextInt(100), random.nextInt(100),userService.getUserByUsername("testUser"+i).get());
        }
    }

    public void fillWithFewNewAccounts() throws TribesInternalServerErrorException, IOException {
        Random random = new Random();
        for (int i=0;i<5;i++) {

            RegisterRequest request = new RegisterRequest();
            request.setUsername("testUser" + i);
            request.setPassword("password" + random.nextInt(200));
            request.setEmail("real.email" + random.nextInt(10000*i+1) + "@tribes.com");
            userService.createUser(request);
        }
    }

    public void fillWithManyNewAccounts() throws TribesInternalServerErrorException, IOException {
        Random random = new Random();
        for (int i=0;i<50;i++) {

            RegisterRequest request = new RegisterRequest();
            request.setUsername("testUser" + i);
            request.setPassword("password" + random.nextInt(200));
            request.setEmail("real.email" + random.nextInt(10000) + "@tribes.com");
            userService.createUser(request);
        }
    }

    public void fillArmy(){
        List<Kingdom> listKingdom = kingdomRepository.findAll();
        for (Kingdom kingdom: listKingdom) {
            kingdomService.fillUnits(kingdom);
        }


    }
}
