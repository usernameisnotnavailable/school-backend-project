package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.dtos.BuildingInfo;
import com.gfa.tribesvibinandtribinotocyon.dtos.BuildingOrUnitProductionCostAndTimeDTO;
import com.gfa.tribesvibinandtribinotocyon.dtos.UnitInfo;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UnitInfoService {

    private static final Map<String, Integer> unitCodes = Stream.of(new Object[][]{
            {"militia", 1},
            {"spearman", 2},
            {"archer", 3},
            {"swordsman", 4},
            {"scout", 5},
            {"crossbowman", 6},
            {"light cavalry", 7},
            {"longbow man", 8},
            {"knight", 9},
            {"heavy cavalry", 10},
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

    private static List<String> lines;

    public UnitInfoService() {
        try {
            lines = Files.readAllLines(Paths.get("./src/main/java/com/gfa/tribesvibinandtribinotocyon/configs/UnitInfo.csv"), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static UnitInfo getUnitInfo(String unitName) {
        try {
            Integer unitIndex = unitCodes.get(unitName.toLowerCase().trim());
            return new UnitInfo(
                    lines
                            .get(unitIndex)
                            .split(",")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static BuildingOrUnitProductionCostAndTimeDTO unitTimeAndCostToTrain(String unitName, Integer unitAmountToTrain) {
        UnitInfo unitInfo = getUnitInfo(unitName);

        if (unitAmountToTrain < 1) {
            return new BuildingOrUnitProductionCostAndTimeDTO(0, 0, 0, 0, 0);
        }

        return new BuildingOrUnitProductionCostAndTimeDTO(
                unitInfo.getFoodCost() * unitAmountToTrain,
                unitInfo.getWoodCost() * unitAmountToTrain,
                unitInfo.getStoneCost() * unitAmountToTrain,
                unitInfo.getIronCost() * unitAmountToTrain,
                unitInfo.getTrainTime() * unitAmountToTrain); //returns and array of [food cost, wood cost, stone cost, iron cost, build time(in minutes)]
    }
}