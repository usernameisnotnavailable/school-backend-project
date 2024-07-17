package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.constants.BuildingConstants;
import com.gfa.tribesvibinandtribinotocyon.constants.TroopConstants;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesNotFoundException;
import com.gfa.tribesvibinandtribinotocyon.models.*;
import com.gfa.tribesvibinandtribinotocyon.repositories.BuildingRepository;
import com.gfa.tribesvibinandtribinotocyon.repositories.KingdomRepository;
import com.gfa.tribesvibinandtribinotocyon.repositories.StockpileRepository;
import com.gfa.tribesvibinandtribinotocyon.repositories.TroopRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class KingdomService {
    private final TroopRepository troopRepository;
    private final StockpileRepository stockpileRepository;
    private final KingdomRepository kingdomRepository;
    private final BuildingRepository buildingRepository;
    private final StockpileService stockpileService;


    public Optional<Kingdom> createKingdom(String name, Integer x, Integer y, UserEntity user) {
        Optional<Kingdom> kingdomOpt = kingdomRepository.findByName(name);

        if (kingdomOpt.isPresent()) {
            return Optional.empty();
        }

        Kingdom kingdomEnt = new Kingdom();
        kingdomEnt.setName(name);
        kingdomEnt.setKingdomLevel(1);
        kingdomEnt.setPeasants(0);
        kingdomEnt.setXCoordinates(x);
        kingdomEnt.setYCoordinates(y);
        kingdomEnt.setUser(user);


        Stockpile stockpile = new Stockpile();
        stockpile.setKingdom(kingdomEnt);
        stockpile.setFood(100);
        stockpile.setWood(100);
        stockpile.setIron(100);
        stockpile.setStone(100);
        stockpileRepository.save(stockpile);
        starterBuilding(kingdomEnt.getId());
        kingdomEnt.setStockpile(stockpile);
        return Optional.of(kingdomRepository.save(kingdomEnt));
    }

    public Optional<Troop> recruitTroops(String name, Kingdom kingdom, int amount) {
        int iron = 0;
        int wood = 0;
        int food = 0;

        switch (name) {
            case "militia":
                food = TroopConstants.MILITIA_FOOD;
                break;
            case "spearman":
                food = TroopConstants.SPEARMAN_FOOD;
                wood = TroopConstants.SPEARMAN_WOOD;
                break;
            case "archer":
                food = TroopConstants.ARCHER_FOOD;
                wood = TroopConstants.ARCHER_WOOD;
                break;
            case "swordsman":
                food = TroopConstants.SWORDSMAN_FOOD;
                wood = TroopConstants.SWORDSMAN_WOOD;
                iron = TroopConstants.SWORDSMAN_IRON;
                break;
            case "scout":
                food = TroopConstants.SCOUT_FOOD;
                iron = TroopConstants.SCOUT_IRON;
                break;
            case "crossbowman":
                food = TroopConstants.CROSSBOWMAN_FOOD;
                wood = TroopConstants.CROSSBOWMAN_WOOD;
                iron = TroopConstants.CROSSBOWMAN_IRON;
                break;
            case "light cavalry":
                food = TroopConstants.LIGHT_CAVALRY_FOOD;
                wood = TroopConstants.LIGHT_CAVALRY_WOOD;
                iron = TroopConstants.LIGHT_CAVALRY_IRON;
                break;
            case "longbow man":
                food = TroopConstants.LONGBOW_MAN_FOOD;
                wood = TroopConstants.LONGBOW_MAN_WOOD;
                iron = TroopConstants.LONGBOW_MAN_IRON;
                break;
            case "knight":
                food = TroopConstants.KNIGHT_FOOD;
                wood = TroopConstants.KNIGHT_WOOD;
                iron = TroopConstants.KNIGHT_IRON;
                break;
            case "heavy cavalry":
                food = TroopConstants.HEAVY_CAVALRY_FOOD;
                wood = TroopConstants.HEAVY_CAVALRY_WOOD;
                iron = TroopConstants.HEAVY_CAVALRY_IRON;
                break;
        }

        Optional<Troop> troopOpt = troopRepository.findByNameAndKingdom_Id(name, kingdom.getId());
        Troop troop;
        if (troopOpt.isEmpty()) {
            troop = new Troop();
            troop.setName(name);
            troop.setKingdom(kingdom);
            troop.setAmount(0);
        } else {
            troop = troopOpt.get();
        }

        Stockpile stockpile = stockpileRepository.findById(kingdom.getStockpile().getId()).get();
        if (stockpile.getFood() >= food * amount &&
                stockpile.getWood() >= wood * amount &&
                stockpile.getIron() >= iron * amount) {
            troop.setAmount(troop.getAmount() + amount);

            stockpile.setFood(stockpile.getFood() - food * amount);
            stockpile.setWood(stockpile.getWood() - wood * amount);
            stockpile.setIron(stockpile.getIron() - iron * amount);

            stockpileRepository.save(stockpile);

            return Optional.of(troopRepository.save(troop));
        } else {
            return Optional.empty();
        }
    }

    public void starterBuilding(Long id) {
        Kingdom kingdom = kingdomRepository.findById(id).get();
        Building farm = new Building();
        farm.setName("Farm");
        farm.setCapacity(BuildingConstants.FARM_CAPACITY);
        farm.setKingdom(kingdom);

        buildingRepository.save(new Building(null, "Farm", BuildingConstants.BUILDING_STARTER_LEVEL, BuildingConstants.BUILDING_STARTER_WORKERS, BuildingConstants.FARM_CAPACITY, kingdom));
        buildingRepository.save(new Building(null, "Woodcutter", BuildingConstants.BUILDING_STARTER_LEVEL, BuildingConstants.BUILDING_STARTER_WORKERS, BuildingConstants.WOODCUTTER_CAPACITY, kingdom));
        buildingRepository.save(new Building(null, "Quarry", BuildingConstants.BUILDING_STARTER_LEVEL, BuildingConstants.BUILDING_STARTER_WORKERS, BuildingConstants.QUARRY_CAPACITY, kingdom));
        buildingRepository.save(new Building(null, "Iron mine", BuildingConstants.BUILDING_STARTER_LEVEL, BuildingConstants.BUILDING_STARTER_WORKERS, BuildingConstants.IRON_MINE_CAPACITY, kingdom));
        buildingRepository.save(new Building(null, "Housing", BuildingConstants.BUILDING_STARTER_LEVEL, BuildingConstants.HOUSING_WORKERS, BuildingConstants.HOUSING_CAPACITY, kingdom));

    }

    public Optional<Building> buildABuilding(String name, Kingdom kingdom) throws TribesNotFoundException {
        Building building = new Building();
        if (checkForValidityAndTakeResources(name, kingdom.getId())) {
            building.setName(name);
            building.setKingdom(kingdom);
            building.setBuildingLevel(1);
            buildingRepository.save(building);
            return Optional.of(building);
        }
        return Optional.empty();
    }

    public boolean checkForValidityAndTakeResources(String name, Long id) throws TribesNotFoundException {
        int iron = 0;
        int wood = 0;
        int food = 0;
        int stone = 0;
        Long stockpileId = stockpileRepository.findStockpileIdByKingdomId(id);

        switch (name) {
            case "Castle":
                food = BuildingConstants.CASTLE_FOOD;
                wood = BuildingConstants.CASTLE_WOOD;
                stone = BuildingConstants.CASTLE_STONE;
                iron = BuildingConstants.CASTLE_IRON;
                break;
            case "Barracks":
                food = BuildingConstants.BARRACKS_FOOD;
                wood = BuildingConstants.BARRACKS_WOOD;
                stone = BuildingConstants.BARRACKS_STONE;
                iron = BuildingConstants.BARRACKS_IRON;
                break;
            case "Wall":
                wood = BuildingConstants.WALL_WOOD;
                stone = BuildingConstants.WALL_STONE;
                iron = BuildingConstants.WALL_STONE;
                break;
            case "Blacksmith":
                food = BuildingConstants.BLACKSMITH_FOOD;
                wood = BuildingConstants.BLACKSMITH_WOOD;
                stone = BuildingConstants.BLACKSMITH_STONE;
                iron = BuildingConstants.BLACKSMITH_IRON;
                break;
        }
        return stockpileService.consumeResources(stockpileId, food, wood, stone, iron);
    }

    public void fillUnits(Kingdom kingdom){
        Random random = new Random();
        Integer ammount = random.nextInt(400);
        troopRepository.save(new Troop("militia",ammount,kingdom));
        ammount = random.nextInt(400);
        troopRepository.save(new Troop("spearman",ammount,kingdom));
        ammount = random.nextInt(300);
        troopRepository.save(new Troop("archer",ammount,kingdom));
        ammount = random.nextInt(200);
        troopRepository.save(new Troop("swordsman",ammount,kingdom));
        ammount = random.nextInt(150);
        troopRepository.save(new Troop("scout",ammount,kingdom));
        ammount = random.nextInt(100);
        troopRepository.save(new Troop("crossbowman",ammount,kingdom));
        ammount = random.nextInt(80);
        troopRepository.save(new Troop("light cavalry",ammount,kingdom));
        ammount = random.nextInt(40);
        troopRepository.save(new Troop("longbow man",ammount,kingdom));
        ammount = random.nextInt(20);
        troopRepository.save(new Troop("knight",ammount,kingdom));
        ammount = random.nextInt(10);
        troopRepository.save(new Troop("heavy cavalry",ammount,kingdom));
    }

    public Optional<Kingdom> findKingdomById(Long id) {
        return kingdomRepository.findById(id);
    }

    @Transactional
    public void calculateHourlyGatheringForAllKingdoms() {
        for (Kingdom kingdom : kingdomRepository.findAll()) {
            int[] resourcesGainedPerHour = calculateHourlyGatheringForKingdom(kingdom);
            Long stockpileId = stockpileRepository.findStockpileIdByKingdomId(kingdom.getId());
            stockpileService.gainResources(stockpileId, resourcesGainedPerHour[0], resourcesGainedPerHour[1], resourcesGainedPerHour[2], resourcesGainedPerHour[3]);
        }
    }

    public int[] calculateHourlyGatheringForKingdom(Kingdom kingdom) {
        int unitsPerWorkerPerHour = 10;
        List<Building> buildings = kingdom.getBuildings();
        int gainedFood = 0;
        int gainedWood = 0;
        int gainedStone = 0;
        int gainedIron = 0;
        for (Building building : buildings) {
            if (building.getName().equalsIgnoreCase("farm")) {
                gainedFood = building.getWorkers() * unitsPerWorkerPerHour;
            } else if (building.getName().equalsIgnoreCase("woodcutter")) {
                gainedWood = building.getWorkers() * unitsPerWorkerPerHour;
            } else if (building.getName().equalsIgnoreCase("quarry")) {
                gainedStone = building.getWorkers() * unitsPerWorkerPerHour;
            } else if (building.getName().equalsIgnoreCase("iron mine")) {
                gainedIron = building.getWorkers() * unitsPerWorkerPerHour;
            }
        }
        return new int[] {gainedFood, gainedWood, gainedStone, gainedIron};
    }

}