package com.gfa.tribesvibinandtribinotocyon.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingInfo {
    String name;
    String description;
    Integer foodBaseCost;
    Integer woodBaseCost;
    Integer stoneBaseCost;
    Integer ironBaseCost;
    Integer startingLvl;
    Integer maxLvl;
    Integer baseBuildTime;
    Integer castleLvlNeeded;

    public BuildingInfo(String[] line) {
        this.name = line[0];
        this.description = line[1];
        this.foodBaseCost = Integer.parseInt(line[2]);
        this.woodBaseCost = Integer.parseInt(line[3]);
        this.stoneBaseCost = Integer.parseInt(line[4]);
        this.ironBaseCost = Integer.parseInt(line[5]);
        this.startingLvl = Integer.parseInt(line[6]);
        this.maxLvl = Integer.parseInt(line[7]);
        this.baseBuildTime = Integer.parseInt(line[8]);
        this.castleLvlNeeded = Integer.parseInt(line[9]);
    }

    public Integer getBuildingTime() {
        return 1;
        // logic has to be implemented here, this is for compilation test purposes only
    }
}