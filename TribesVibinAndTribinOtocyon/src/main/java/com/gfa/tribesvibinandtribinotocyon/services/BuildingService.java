package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.dtos.BuildingOrUnitProductionCostAndTimeDTO;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesBadRequestException;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesForbiddenException;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesInternalServerErrorException;
import com.gfa.tribesvibinandtribinotocyon.models.Building;
import com.gfa.tribesvibinandtribinotocyon.repositories.BuildingRepository;
import com.gfa.tribesvibinandtribinotocyon.repositories.KingdomRepository;
import lombok.RequiredArgsConstructor;
import com.gfa.tribesvibinandtribinotocyon.dtos.BuildingDTO;
import com.gfa.tribesvibinandtribinotocyon.models.Kingdom;

import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class BuildingService {
    private final BuildingRepository buildingRepository;
    private final KingdomRepository kingdomRepository;
    private final StockpileService stockpileService;
    private final TimeService timeService;


    public Date upgradeBuilding(Long kingdomId, String buildingName) throws TribesInternalServerErrorException, TribesBadRequestException, TribesForbiddenException {
        Date finishTime;

        if (kingdomRepository.findById(kingdomId).isPresent()) {
            Optional<Building> building = buildingRepository.getBuildingByKingdomIdAndName(kingdomId, buildingName);
            if (building.isPresent()) {
                Integer levelToBuild = building.get().getBuildingLevel() + 1;
                Long stockPileId = kingdomRepository.findById(kingdomId).get().getStockpile().getId();
                BuildingOrUnitProductionCostAndTimeDTO buildingCostAndTimeDTO = BuildingInfoService.buildTimeAndCostForLvl(buildingName, levelToBuild);
                if (buildingCostAndTimeDTO.getBuildingTime() > 0) {
                    if (stockpileService.hasEnoughResourcesForBuilding(stockPileId, buildingName, levelToBuild)) {
                        stockpileService.takeResources(stockPileId,
                                buildingCostAndTimeDTO.getWoodCost(),
                                buildingCostAndTimeDTO.getFoodCost(),
                                buildingCostAndTimeDTO.getIronCost(),
                                buildingCostAndTimeDTO.getStoneCost());
                        finishTime = timeService.build(kingdomId, buildingName);
                    } else {
                        throw new TribesBadRequestException("Not enough resources to build.");
                    }
                } else {
                    throw new TribesBadRequestException("Building level exceeded.");
                }
            } else {
                throw new TribesBadRequestException("Building not found.");
            }
        } else {
            throw new TribesBadRequestException("Invalid kingdom Id.");
        }
        return finishTime;
    }


        public List<BuildingDTO> getBuildingList (Kingdom kingdom){
            return buildingRepository.findAllByKingdom(kingdom).stream().map(BuildingDTO::new).toList();
        }
        public List<Building> getAllBuildings (Long kingdomId){
            if (kingdomRepository.findById(kingdomId).isPresent()) {
                return kingdomRepository.findById(kingdomId)
                        .get()
                        .getBuildings();
            }
            return List.of();
        }


}
