package com.gfa.tribesvibinandtribinotocyon.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class BuildingResponse {
    private Long KingdomId;
    private String buildingName;
    private Date buildingEndDate;
}
