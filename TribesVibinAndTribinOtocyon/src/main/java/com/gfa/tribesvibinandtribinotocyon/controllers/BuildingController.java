package com.gfa.tribesvibinandtribinotocyon.controllers;


import com.gfa.tribesvibinandtribinotocyon.dtos.BuildingResponse;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesBadRequestException;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesForbiddenException;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesInternalServerErrorException;
import com.gfa.tribesvibinandtribinotocyon.services.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController("/kingdom")
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;


    @PostMapping("/{kingdomId}/building/{buildingName}")
    public ResponseEntity<?> building(@PathVariable Long kingdomId, @PathVariable String buildingName) throws TribesInternalServerErrorException, TribesBadRequestException, TribesForbiddenException {

        BuildingResponse buildingResponse = new BuildingResponse();
        buildingResponse.setBuildingName(buildingName);
        buildingResponse.setKingdomId(kingdomId);
        buildingResponse.setBuildingEndDate(buildingService.upgradeBuilding(kingdomId, buildingName));
            return ResponseEntity.ok().body(buildingResponse);
    }

}
