package com.gfa.tribesvibinandtribinotocyon.dtos;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BuildingOrUnitProductionCostAndTimeDTO {
    Integer foodCost;
    Integer woodCost;
    Integer stoneCost;
    Integer ironCost;
    Integer buildingTime;

    public BuildingOrUnitProductionCostAndTimeDTO(Integer foodCost, Integer woodCost, Integer stoneCost, Integer ironCost, Integer buildingTime) {
        this.foodCost = foodCost;
        this.woodCost = woodCost;
        this.stoneCost = stoneCost;
        this.ironCost = ironCost;
        this.buildingTime = buildingTime;
    }
}
