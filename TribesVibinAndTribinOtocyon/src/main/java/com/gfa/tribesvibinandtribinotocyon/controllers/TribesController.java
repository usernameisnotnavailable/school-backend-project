package com.gfa.tribesvibinandtribinotocyon.controllers;

import com.gfa.tribesvibinandtribinotocyon.dtos.AdminAccessKingdomDTO;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesBadRequestException;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesUnauthorizedException;
import com.gfa.tribesvibinandtribinotocyon.models.Kingdom;
import com.gfa.tribesvibinandtribinotocyon.models.UserEntity;
import com.gfa.tribesvibinandtribinotocyon.models.roles.RoleAdmin;
import com.gfa.tribesvibinandtribinotocyon.models.roles.RoleUser;
import com.gfa.tribesvibinandtribinotocyon.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/tribes")
@RequiredArgsConstructor
@RestController
public class TribesController {

    private final KingdomService kingdomService;
    private final BuildingService buildingService;
    private final UserService userService;
    private final StockpileService stockpileService;
    private final TroopService troopService;

    @GetMapping("/kingdom/{id}/buildings")
    public ResponseEntity<?> findKingdomBuildingsList(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) throws TribesBadRequestException, TribesUnauthorizedException {

        Optional<UserEntity> userEntity = userService.getUserByUsername(user.getUsername());

        if (userEntity.isEmpty() ||
                (!userEntity.get().getRole().getClass().equals(RoleUser.class)
                        && !userEntity.get().getRole().getClass().equals(RoleAdmin.class))) {
            throw new TribesUnauthorizedException("User is not authorized!");
        }

        if (id == null || id < 0) {
            throw new TribesBadRequestException("Please provide valid id");
        }

        Optional<Kingdom> kingdom = kingdomService.findKingdomById(id);

        if (kingdom.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(buildingService.getBuildingList(kingdom.get()));
    }

    @GetMapping("/kingdom/{id}")
    public ResponseEntity<?> adminKingdomAccess(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) throws TribesUnauthorizedException, TribesBadRequestException {


        Optional<UserEntity> userEntity = userService.getUserByUsername(user.getUsername());

        if (userEntity.isEmpty() || !userEntity.get().getRole().getClass().equals(RoleAdmin.class)) {
            throw new TribesUnauthorizedException("User is not authorized!");
        }

        Optional<Kingdom> kingdom = kingdomService.findKingdomById(id);

        if (id == null || id < 0 || kingdom.isEmpty()) {
            throw new TribesBadRequestException("Please provide valid id");
        }

        return ResponseEntity.ok(
                new AdminAccessKingdomDTO(
                        buildingService.getBuildingList(kingdom.get()),
                        stockpileService.getStockpileOfKingdom(kingdom.get()),
                        troopService.findTroopsOfKingdom(kingdom.get())));

    }
}