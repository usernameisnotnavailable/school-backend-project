package com.gfa.tribesvibinandtribinotocyon.services;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BuildingLvlInfoService {

    private static final Map<String, Integer> buildingCodes = Stream.of(new Object[][]{
            {"castle", 4},
            {"farm", 1},
            {"woodcutter", 1},
            {"quarry", 1},
            {"iron mine", 1},
            {"housing", 2},
            {"stockpile", 3},
            {"barracks", 6},
            {"wall", 5},
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

    private static List<String> lines;

    public BuildingLvlInfoService() {

        try {
            lines = Files.readAllLines(Paths.get("./src/main/java/com/gfa/tribesvibinandtribinotocyon/configs/BuildingLvlDetails.csv"), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String[] getBuildingInfo(Integer lvl) {

        if (lvl < 1) {
            throw new RuntimeException();
        }

        try {
            return lines
                    .get(lvl)
                    .split(",");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param lvl level of the building you need info of
     * @return the information of the building lvl
     */
    public static Integer getProductionBuildingWorkerSlotsForLvl(Integer lvl) {
        if (lvl > 10) {
            return -1;
        }
        return Integer.parseInt(getBuildingInfo(lvl)[1]);
    }

    public static Integer getHousingWorkersForLvl(Integer lvl) {
        if (lvl > 10) {
            return -1;
        }
        return Integer.parseInt(getBuildingInfo(lvl)[2]);
    }

    public static Integer getStockpileCapacityForLvl(Integer lvl) {
        if (lvl > 5) {
            return -1;
        }
        return Integer.parseInt(getBuildingInfo(lvl)[3]);
    }

    public static Integer getCastleBuildTimeModifierForLvl(Integer lvl) {
        if (lvl > 5) {
            return -1;
        }
        return Integer.parseInt(getBuildingInfo(lvl)[4]);
    }

    public static Integer getWallDefenseModifierForLvl(Integer lvl) {
        if (lvl > 5) {
            return -1;
        }
        return Integer.parseInt(getBuildingInfo(lvl)[5]);
    }

    public static String getBarrackUnlockedUnitForLvl(Integer lvl) {
        return getBuildingInfo(lvl)[6];
    }
}