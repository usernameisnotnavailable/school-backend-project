package com.gfa.tribesvibinandtribinotocyon.controllers;

import com.gfa.tribesvibinandtribinotocyon.dtos.TroopsDto;
import com.gfa.tribesvibinandtribinotocyon.services.TroopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TroopController {

    private final TroopService troopService;

    @GetMapping("/kingdom/troops")
    public ResponseEntity<?> getTroopsFromKingdom(){
        TroopsDto troops = new TroopsDto();
        troops.setTroops(troopService.getAllTroops(SecurityContextHolder.getContext().getAuthentication().getName()));
       return ResponseEntity.ok().body(troops);
    }
}
