package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.models.Building;
import com.gfa.tribesvibinandtribinotocyon.models.Troop;
import com.gfa.tribesvibinandtribinotocyon.repositories.BuildingRepository;
import com.gfa.tribesvibinandtribinotocyon.repositories.KingdomRepository;
import com.gfa.tribesvibinandtribinotocyon.repositories.TroopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExecutorService {
    private final TroopRepository troopRepository;
    private final KingdomRepository kingdomRepository;
    private final BuildingRepository buildingRepository;

    public void actualTroopRecruit(Long kingdomId, String troopName, Integer troopAmount) {
        List<Troop> listOfTroops = troopRepository.findTroopsByKingdomId(kingdomId);

        listOfTroops.stream().filter(troop -> troop.getName().equals(troopName)).findFirst().ifPresentOrElse(troop -> troop.setAmount(troop.getAmount() + troopAmount), () -> {
            Troop newTroop = new Troop();
            newTroop.setName(troopName);
            newTroop.setAmount(troopAmount);
            listOfTroops.add(newTroop);
        });

        troopRepository.saveAll(listOfTroops);
        kingdomRepository.findById(kingdomId).ifPresent(kingdomRepository::save);
    }

    public void actualTroopMovement(Long userId, List<Troop> troops, Integer targetXCoordinates, Integer targetYCoordinates) {

    }
    public void actualUpgradeBuilding(Long kingdomId, String buildingName, Integer levelToBuild) {
        Optional<Building> building = buildingRepository.getBuildingByKingdomIdAndName(kingdomId, buildingName);
        if (building.isPresent()) {
            building.ifPresent(upgrade -> upgrade.setBuildingLevel(levelToBuild));
            buildingRepository.save(building.get());
        }
    }

}
