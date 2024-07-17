package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.dtos.BuildingOrUnitProductionCostAndTimeDTO;
import com.gfa.tribesvibinandtribinotocyon.dtos.BuildingInfo;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesForbiddenException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BuildingInfoService {

    private static final Map<String, Integer> buildingCodes = Stream
            .of(new Object[][]{{"castle", 1},
                    {"farm", 2},
                    {"woodcutter", 3},
                    {"quarry", 4},
                    {"iron mine", 5},
                    {"housing", 6},
                    {"stockpile", 7},
                    {"barracks", 8},
                    {"wall", 9},
                    {"blacksmith", 10},})
            .collect(Collectors
                    .toMap(data -> (String) data[0], data -> (Integer) data[1]));

    private static List<String> lines;

    public BuildingInfoService() {
        try {
            lines = Files.readAllLines(Paths.get("./src/main/java/com/gfa/tribesvibinandtribinotocyon/configs/BuildingInfo.csv"), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param buildingName: name of the building
     *                      1 Castle
     *                      2 Farm
     *                      3 Woodcutter
     *                      4 Quarry
     *                      5 Iron mine
     *                      6 Housing
     *                      7 Stockpile
     *                      8 Barracks
     *                      9 Wall
     *                      10 Blacksmith
     * @return :all the details of buildings in a single DTO
     */

    public static BuildingInfo getBuildingInfo(String buildingName) {
        try {
            for (int i = 1; i < lines.size(); i++) {
                String[] line = lines.get(i).split(",");
                if (line[0].equalsIgnoreCase(buildingName.trim())) {
                    return new BuildingInfo(line);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param buildingName name of building as seen above
     * @param lvlToBuild   the level you want to build your building to
     * @return the cost and time needed to build it as a DTO
     */

    public static BuildingOrUnitProductionCostAndTimeDTO buildTimeAndCostForLvl(String buildingName, Integer lvlToBuild) throws TribesForbiddenException {
        BuildingInfo buildingInfo = getBuildingInfo(buildingName);

        if (buildingInfo == null || lvlToBuild > buildingInfo.getMaxLvl()) {
            throw new TribesForbiddenException("invalid building name or level provided in building cost calculator");
        }

        int multiplier = 1;
        for (int i = lvlToBuild - buildingInfo.getStartingLvl(); i > 1; i--) {
            multiplier *= 2;
        }

        return new BuildingOrUnitProductionCostAndTimeDTO(
                buildingInfo.getFoodBaseCost() * multiplier,
                buildingInfo.getWoodBaseCost() * multiplier,
                buildingInfo.getStoneBaseCost() * multiplier,
                buildingInfo.getIronBaseCost() * multiplier,
                buildingInfo.getBaseBuildTime() * multiplier); //returns and array of [food cost, wood cost, stone cost, iron cost, build time(in minutes)]
    }
}
