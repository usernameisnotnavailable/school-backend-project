package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.models.Kingdom;
import com.gfa.tribesvibinandtribinotocyon.models.Troop;
import com.gfa.tribesvibinandtribinotocyon.repositories.TroopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TroopService {

    private final TroopRepository troopRepository;

    public void actualTroopRecruit(Long userId, String troopName, Integer troopAmount) {
    }

    public void actualTroopMovement(Long userId, List<Troop> troops, Integer targetXCoordinates, Integer targetYCoordinates) {
    }

    public List<Troop> getAllTroops(String email){
        return troopRepository.findTroopsByEmail(email);
    }

    public List<Troop> findTroopsOfKingdom(Kingdom kingdom) {
        return troopRepository.findAllByKingdom(kingdom);
    }
}