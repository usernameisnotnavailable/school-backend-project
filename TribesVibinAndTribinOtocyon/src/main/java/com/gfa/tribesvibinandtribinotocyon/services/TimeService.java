package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.dtos.BuildingInfo;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesInternalServerErrorException;
import com.gfa.tribesvibinandtribinotocyon.models.Troop;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TimeService {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private static final int MILLISECONDS_IN_SECOND = 1000; // 1 sec = 1000 milliseconds
    private static final int SECONDS_IN_MINUTE = 60; // 1 min = 60 sec
    private static final int MINUTES_IN_HOUR = 60; // 1 hour = 60 min

    private final KingdomService kingdomService;
    private final TroopService troopService;
    private final ExecutorService executorService;


    public Date build(Long userId, String buildingName) throws TribesInternalServerErrorException {
        BuildingInfo buildingInfo = BuildingInfoService.getBuildingInfo(buildingName);
        if (buildingInfo != null) {
            Integer buildingTime = buildingInfo.getBuildingTime();

            Runnable builder = () -> executorService.actualUpgradeBuilding(userId, buildingName, 0);

            ScheduledFuture<?> builderHandle = scheduler.schedule(builder, buildingTime, TimeUnit.MINUTES);

            Date currentDate = new Date();
            long buildingTimeDate = MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE * buildingTime;
            return new Date(currentDate.getTime() + buildingTimeDate);
        } else {
            throw new TribesInternalServerErrorException();
        }
    }

    public Date recruit(Long userId, String troopName, Integer troopAmount) {
        Integer recruitingTime = UnitInfoService.getUnitInfo(troopName).getTrainTime();
        recruitingTime *= troopAmount;

        Runnable recruiter = () -> executorService.actualTroopRecruit(userId, troopName, troopAmount);

        ScheduledFuture<?> recruitHandle = scheduler.schedule(recruiter, recruitingTime, TimeUnit.MINUTES);

        Date currentDate = new Date();
        long recruitingTimeDate = MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE * recruitingTime;
        return new Date(currentDate.getTime() + recruitingTimeDate);
    }

    public Date move(Long userId, Integer baseXCoordinates, Integer baseYCoordinates, Integer targetXCoordinates, Integer targetYCoordinates, List<Troop> troops) {

        Integer slowestUnit = troops.stream()
                .reduce((troop1, troop2) -> UnitInfoService.getUnitInfo(troop1.getName()).getSpeed() < UnitInfoService.getUnitInfo(troop2.getName()).getSpeed() ? troop1 : troop2)
                .map(troop -> UnitInfoService.getUnitInfo(troop.getName()).getSpeed())
                .orElse(1);

        Float marchingDistance = (float) Math.pow(Math.pow((baseXCoordinates - targetXCoordinates), 2) + (Math.pow((baseYCoordinates - targetYCoordinates), 2)), 0.5);

        Float marchingTime = marchingDistance / slowestUnit;
        Long marchingTimeInSeconds = (long) (marchingTime * SECONDS_IN_MINUTE * MINUTES_IN_HOUR);

        Runnable mover = () -> executorService.actualTroopMovement(userId, troops, targetXCoordinates, targetYCoordinates);

        ScheduledFuture<?> movementHandle = scheduler.schedule(mover, marchingTimeInSeconds, TimeUnit.SECONDS);

        Date currentDate = new Date();
        float marchingTimeDate = MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * marchingTime;
        return new Date(currentDate.getTime() + (long) marchingTimeDate);
    }

    @PostConstruct
    public Date hourlyResourceGathering() {
        Runnable gatherer = () -> kingdomService.calculateHourlyGatheringForAllKingdoms();

        scheduler.scheduleAtFixedRate(gatherer, 0, MINUTES_IN_HOUR, TimeUnit.MINUTES);
        Date currentDate = new Date();
        long gatheringTimeDate = MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR;
        return new Date(currentDate.getTime() + gatheringTimeDate);
    }

}