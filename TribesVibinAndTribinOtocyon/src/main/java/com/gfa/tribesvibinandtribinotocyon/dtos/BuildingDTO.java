package com.gfa.tribesvibinandtribinotocyon.dtos;

import com.gfa.tribesvibinandtribinotocyon.models.Building;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BuildingDTO {
    String name;
    Integer buildingLvl;

    public BuildingDTO(Building building) {
        this.name = building.getName();
        this.buildingLvl = building.getBuildingLevel();
    }
}